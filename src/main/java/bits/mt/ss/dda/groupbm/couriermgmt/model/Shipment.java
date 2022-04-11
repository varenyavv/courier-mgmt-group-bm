package bits.mt.ss.dda.groupbm.couriermgmt.model;

public class Shipment {

  private final long sourcePincode;
  private final long destPincode;
  private final int lengthInCm;
  private final int widthInCm;
  private final int heightInCm;
  private final int weightInGram;

  public Shipment(
      long sourcePincode,
      long destPincode,
      int lengthInCm,
      int widthInCm,
      int heightInCm,
      int weightInGram) {
    this.sourcePincode = sourcePincode;
    this.destPincode = destPincode;
    this.lengthInCm = lengthInCm;
    this.widthInCm = widthInCm;
    this.heightInCm = heightInCm;
    this.weightInGram = weightInGram;
  }

  public long getSourcePincode() {
    return sourcePincode;
  }

  public long getDestPincode() {
    return destPincode;
  }

  public int getLengthInCm() {
    return lengthInCm;
  }

  public int getWidthInCm() {
    return widthInCm;
  }

  public int getHeightInCm() {
    return heightInCm;
  }

  public int getWeightInGram() {
    return weightInGram;
  }
}
