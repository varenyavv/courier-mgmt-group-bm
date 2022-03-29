package bits.mt.ss.dda.groupbm.couriermgmt.service;

import bits.mt.ss.dda.groupbm.couriermgmt.exception.ServiceUnavailableException;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Route;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Shipment;

@FunctionalInterface
public interface RoutingEngine {

  // Metric Shipping Factor in cubic centimeter per kilogram
  long MSF = 5000;

  // base weight
  int BASE_WEIGHT_IN_KG = 1;

  Route findRoute(Shipment shipment) throws ServiceUnavailableException;

  default int getPackageExtraWeightMultiplier(
      double lengthInCm, double widthInCm, double heightInCm, double packageWeightInGram) {
    double effectiveWeight =
        Math.ceil(
            Math.max(packageWeightInGram / 1000, ((lengthInCm * widthInCm * heightInCm) / MSF)));
    if (effectiveWeight <= BASE_WEIGHT_IN_KG) {
      return 0;
    } else {
      return (int) effectiveWeight - BASE_WEIGHT_IN_KG;
    }
  }
}
