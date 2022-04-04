package bits.mt.ss.dda.groupbm.couriermgmt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bits.mt.ss.dda.groupbm.couriermgmt.dao.BranchDao;
import bits.mt.ss.dda.groupbm.couriermgmt.dao.RouterDao;
import bits.mt.ss.dda.groupbm.couriermgmt.enums.TransportMode;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.CommonErrors;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.ServiceUnavailableException;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Branch;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Distance;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Hop;
import bits.mt.ss.dda.groupbm.couriermgmt.model.PincodeBranch;
import bits.mt.ss.dda.groupbm.couriermgmt.model.RateCard;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Route;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Shipment;

@Service
public class RandomRouteAllocator implements RoutingEngine {

  private static final Logger LOGGER = LoggerFactory.getLogger(RandomRouteAllocator.class);

  @Autowired RouterDao routerDao;

  @Autowired BranchDao branchDao;

  static Random random = new Random();

  static final int MINIMUM_NON_AIR_DISTANCE = 200;
  static final int LOCAL_AREA_DISTANCE = 10;

  public Distance findDistance(long sourcePincode, long destinationPincode)
      throws ServiceUnavailableException {
    LOGGER.debug("findDistance operation invoked");

    Distance distanceInDb =
        routerDao.getDistanceBetweenSourceAndDestination(sourcePincode, destinationPincode);

    if (null != distanceInDb) {
      return distanceInDb;
    }

    List<PincodeBranch> sourceAndDestBranches =
        routerDao.getSourceAndDestinationBranchByPincodes(sourcePincode, destinationPincode);

    Optional<String> sourceBranchCode =
        sourceAndDestBranches.stream()
            .filter(pincodeBranch -> Objects.equals(sourcePincode, pincodeBranch.getPincode()))
            .map(PincodeBranch::getBranchCode)
            .findFirst();

    Optional<String> destBranchCode =
        sourceAndDestBranches.stream()
            .filter(pincodeBranch -> Objects.equals(destinationPincode, pincodeBranch.getPincode()))
            .map(PincodeBranch::getBranchCode)
            .findFirst();

    if (sourceBranchCode.isEmpty()) {
      throw new ServiceUnavailableException(
          CommonErrors.SERVICE_NOT_AVAILABLE,
          "Sorry, our services are not yet available in your area!");
    } else if (destBranchCode.isEmpty()) {
      throw new ServiceUnavailableException(
          CommonErrors.SERVICE_NOT_AVAILABLE,
          "Sorry, our services are not yet available in the provided destination!");
    }

    Long distanceReverse =
        routerDao.getDistanceInKmBetweenSourceAndDestination(destinationPincode, sourcePincode);

    long distance;
    if (Objects.equals(sourcePincode, destinationPincode)
        || Objects.equals(sourceBranchCode.get(), destBranchCode.get())) {
      distance = LOCAL_AREA_DISTANCE;
    } else {
      // random distance between 51 and 10000 km
      distance =
          Objects.requireNonNullElseGet(distanceReverse, () -> (long) random.nextInt(9950) + 51);
    }
    routerDao.saveDistance(sourcePincode, destinationPincode, distance);
    return new Distance(
        sourcePincode, destinationPincode, sourceBranchCode.get(), destBranchCode.get(), distance);
  }

  public Route getQuote(Shipment shipment) throws ServiceUnavailableException {

    LOGGER.debug("getQuote operation invoked");

    Distance distance = findDistance(shipment.getSourcePincode(), shipment.getDestPincode());

    Double totalCost = null;
    if ((shipment.getLengthInCm() > 0 && shipment.getWidthInCm() > 0)
        || shipment.getWeightInGram() > 0) {
      totalCost = getTotalCost(shipment, distance);
    }

    return new Route(null, totalCost).setTotalDistance(distance.getDistanceInKm());
  }

  @Override
  public Route findRoute(Shipment shipment) throws ServiceUnavailableException {

    LOGGER.debug("findRoute operation invoked");

    Distance distance = findDistance(shipment.getSourcePincode(), shipment.getDestPincode());

    double totalCost = getTotalCost(shipment, distance);

    List<Hop> hops =
        routerDao.getRouteBetweenSourceAndDestination(
            shipment.getSourcePincode(), shipment.getDestPincode());

    if (ObjectUtils.isEmpty(hops)) {
      hops = new ArrayList<>();
      Branch sourceBranch = branchDao.getBranchByBranchCode(distance.getSourceBranchCode());
      if (Objects.equals(shipment.getSourcePincode(), shipment.getDestPincode())
          || Objects.equals(distance.getSourceBranchCode(), distance.getDestinationBranchCode())) {
        // some local area delivery
        hops.add(new Hop(sourceBranch, null, 0));
      } else {

        Branch destinationBranch =
            branchDao.getBranchByBranchCode(distance.getDestinationBranchCode());

        int intermediateBranchCount = 0;
        List<Branch> intermediateBranches = null;
        if (distance.getDistanceInKm() > MINIMUM_NON_AIR_DISTANCE) {
          intermediateBranches =
              routerDao.getNRandomIntermediateBranchFromSourceToDest(
                  distance.getSourceBranchCode(),
                  distance.getDestinationBranchCode(),
                  random.nextInt(2) + 1);
          intermediateBranchCount = intermediateBranches.size();
        }

        if (distance.getDistanceInKm() <= MINIMUM_NON_AIR_DISTANCE
            || intermediateBranchCount == 0) {
          TransportMode transportMode;
          if (distance.getDistanceInKm() <= MINIMUM_NON_AIR_DISTANCE) {
            transportMode = TransportMode.ROAD;
          } else {
            transportMode = random.nextBoolean() ? TransportMode.AIR : TransportMode.ROAD;
          }
          hops.add(new Hop(sourceBranch, transportMode, 0));
          hops.add(new Hop(destinationBranch, null, 1));
          routerDao.saveRoute(shipment.getSourcePincode(), shipment.getDestPincode(), hops);
          return new Route(hops, totalCost).setTotalDistance(distance.getDistanceInKm());
        }

        int hopCount = 0;
        hops.add(new Hop(sourceBranch, getRandomTransportationMode(hopCount), hopCount));
        for (Branch intermediateBranch : intermediateBranches) {
          ++hopCount;
          hops.add(new Hop(intermediateBranch, getRandomTransportationMode(hopCount), hopCount));
        }
        hops.add(new Hop(destinationBranch, null, ++hopCount));
      }
      routerDao.saveRoute(shipment.getSourcePincode(), shipment.getDestPincode(), hops);
    }

    return new Route(hops, totalCost).setTotalDistance(distance.getDistanceInKm());
  }

  private double getTotalCost(Shipment shipment, Distance distance) {
    int extraWeightMultiplier =
        getPackageExtraWeightMultiplier(
            shipment.getLengthInCm(),
            shipment.getWidthInCm(),
            shipment.getHeightInCm(),
            shipment.getWeightInGram());
    return getTotalCost(extraWeightMultiplier, distance.getDistanceInKm());
  }

  private TransportMode getRandomTransportationMode(int hopCount) {
    if (hopCount > 1) {
      return TransportMode.ROAD;
    } else {
      return random.nextBoolean() ? TransportMode.AIR : TransportMode.RAILWAY;
    }
  }

  private double getTotalCost(int extraWeightMultiplier, double distance) {
    RateCard rateCard = routerDao.getRateCardByDistance(distance);

    return rateCard.getBaseRate() + (extraWeightMultiplier * rateCard.getExtraWeightFactor());
  }
}
