package bits.mt.ss.dda.groupbm.couriermgmt.model;

public class RateCard {

  private final double baseRate;
  private final double expressModeFactor;
  private final double extraWeightFactor;

  public RateCard(double baseRate, double expressModeFactor, double extraWeightFactor) {
    this.baseRate = baseRate;
    this.expressModeFactor = expressModeFactor;
    this.extraWeightFactor = extraWeightFactor;
  }

  public double getBaseRate() {
    return baseRate;
  }

  public double getExpressModeFactor() {
    return expressModeFactor;
  }

  public double getExtraWeightFactor() {
    return extraWeightFactor;
  }
}
