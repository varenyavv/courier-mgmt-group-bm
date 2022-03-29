package bits.mt.ss.dda.groupbm.couriermgmt.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bits.mt.ss.dda.groupbm.couriermgmt.dao.BranchDao;
import bits.mt.ss.dda.groupbm.couriermgmt.dao.RouterDao;
import bits.mt.ss.dda.groupbm.couriermgmt.enums.DeliveryMode;
import bits.mt.ss.dda.groupbm.couriermgmt.enums.TransportMode;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.CommonErrors;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.ServiceUnavailableException;
import bits.mt.ss.dda.groupbm.couriermgmt.model.*;

@Service
public class RandomRouteAllocator implements RoutingEngine {

  private static final Logger LOGGER = LoggerFactory.getLogger(RandomRouteAllocator.class);

  @Autowired RouterDao routerDao;

  @Autowired BranchDao branchDao;

  static Random random = new Random();

  static final int MINIMUM_NON_AIR_DISTANCE = 200;

  @Override
  public Route findRoute(Shipment shipment) throws ServiceUnavailableException {

    LOGGER.debug("findRoute operation invoked");

    List<PincodeBranch> sourceAndDestBranches =
        routerDao.getSourceAndDestinationBranchByPincodes(
            shipment.getSourcePincode(), shipment.getDestPincode());

    Optional<String> sourceBranchCode =
        sourceAndDestBranches.stream()
            .filter(
                pincodeBranch ->
                    Objects.equals(shipment.getSourcePincode(), pincodeBranch.getPincode()))
            .map(PincodeBranch::getBranchCode)
            .findFirst();

    Optional<String> destBranchCode =
        sourceAndDestBranches.stream()
            .filter(
                pincodeBranch ->
                    Objects.equals(shipment.getDestPincode(), pincodeBranch.getPincode()))
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

    int extraWeightMultiplier =
        getPackageExtraWeightMultiplier(
            shipment.getLengthInCm(),
            shipment.getWidthInCm(),
            shipment.getHeightInCm(),
            shipment.getWeightInGram());
    double baseRate = 0;

    List<Hop> hops = new LinkedList<>();
    if (Objects.equals(shipment.getSourcePincode(), shipment.getDestPincode())
        || Objects.equals(sourceBranchCode.get(), destBranchCode.get())) {
      // some local area delivery
      double totalCost = getTotalCost(shipment.getDeliveryMode(), extraWeightMultiplier, baseRate);

      Branch sourceBranch = branchDao.getBranchByBranchCode(sourceBranchCode.get());
      hops.add(new Hop(sourceBranch, null));
      return new Route(hops, totalCost).setTotalDistance(5);
    } else {

      double distance = random.nextInt(9950) + 51; // random distance between 51 and 10000 km
      double totalCost = getTotalCost(shipment.getDeliveryMode(), extraWeightMultiplier, distance);

      Branch sourceBranch = branchDao.getBranchByBranchCode(sourceBranchCode.get());
      Branch destinationBranch = branchDao.getBranchByBranchCode(destBranchCode.get());

      int intermediateBranchCount = 0;
      List<Branch> intermediateBranches = null;
      if (distance > MINIMUM_NON_AIR_DISTANCE) {
        intermediateBranches =
            routerDao.getNRandomIntermediateBranchFromSourceToDest(
                sourceBranchCode.get(), destBranchCode.get(), random.nextInt(4) + 1);
        intermediateBranchCount = intermediateBranches.size();
      }

      if (distance <= MINIMUM_NON_AIR_DISTANCE || intermediateBranchCount == 0) {
        TransportMode transportMode;
        if (distance <= MINIMUM_NON_AIR_DISTANCE) {
          transportMode = TransportMode.ROAD;
        } else if (DeliveryMode.EXPRESS.equals(shipment.getDeliveryMode())) {
          transportMode = random.nextBoolean() ? TransportMode.AIR : TransportMode.ROAD;
        } else {
          transportMode = TransportMode.ROAD;
        }
        hops.add(new Hop(sourceBranch, transportMode));
        hops.add(new Hop(destinationBranch, null));
        return new Route(hops, totalCost).setTotalDistance(distance);
      }

      int hopCount = 1;
      hops.add(
          new Hop(sourceBranch, getRandomTransportationMode(shipment.getDeliveryMode(), hopCount)));
      for (Branch intermediateBranch : intermediateBranches) {
        hops.add(
            new Hop(
                intermediateBranch,
                getRandomTransportationMode(shipment.getDeliveryMode(), ++hopCount)));
      }
      hops.add(new Hop(destinationBranch, null));
      return new Route(hops, totalCost).setTotalDistance(distance);
    }
  }

  private TransportMode getRandomTransportationMode(DeliveryMode deliveryMode, int hopCount) {
    if (hopCount > 2) {
      return TransportMode.ROAD;
    }
    if (hopCount == 1) {
      if (DeliveryMode.EXPRESS.equals(deliveryMode)) {
        return TransportMode.AIR;
      } else {
        return random.nextBoolean() ? TransportMode.RAILWAY : TransportMode.ROAD;
      }
    } else {
      if (DeliveryMode.EXPRESS.equals(deliveryMode)) {
        return random.nextBoolean() ? TransportMode.AIR : TransportMode.ROAD;
      }
      return random.nextBoolean() ? TransportMode.RAILWAY : TransportMode.ROAD;
    }
  }

  private double getTotalCost(
      DeliveryMode deliveryMode, int extraWeightMultiplier, double distance) {
    RateCard rateCard = routerDao.getRateCardByDistance(distance);

    double baseRate;
    if (DeliveryMode.EXPRESS.equals(deliveryMode)) {
      baseRate = rateCard.getBaseRate() * rateCard.getExpressModeFactor();
    } else {
      baseRate = rateCard.getBaseRate();
    }
    return baseRate + (extraWeightMultiplier * rateCard.getExtraWeightFactor());
  }
}
