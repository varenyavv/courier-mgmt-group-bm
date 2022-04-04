package bits.mt.ss.dda.groupbm.couriermgmt.model;

public class RateCard {

  private final double baseRate;
  private final double extraWeightFactor;

  public RateCard(double baseRate, double extraWeightFactor) {
    this.baseRate = baseRate;
    this.extraWeightFactor = extraWeightFactor;
  }

  public double getBaseRate() {
    return baseRate;
  }

  public double getExtraWeightFactor() {
    return extraWeightFactor;
  }
}
