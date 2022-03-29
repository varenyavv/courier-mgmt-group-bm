package bits.mt.ss.dda.groupbm.couriermgmt.model;

import bits.mt.ss.dda.groupbm.couriermgmt.enums.DeliveryMode;

public class Shipment {

  private final long sourcePincode;
  private final long destPincode;
  private final int lengthInCm;
  private final int widthInCm;
  private final int heightInCm;
  private final int weightInGram;
  private final DeliveryMode deliveryMode;
  private String sourceAddress;
  private String destinationAddress;

  public Shipment(
      long sourcePincode,
      long destPincode,
      int lengthInCm,
      int widthInCm,
      int heightInCm,
      int weightInGram,
      DeliveryMode deliveryMode) {
    this.sourcePincode = sourcePincode;
    this.destPincode = destPincode;
    this.lengthInCm = lengthInCm;
    this.widthInCm = widthInCm;
    this.heightInCm = heightInCm;
    this.weightInGram = weightInGram;
    this.deliveryMode = deliveryMode;
  }

  public void setSourceAddress(String sourceAddress) {
    this.sourceAddress = sourceAddress;
  }

  public void setDestinationAddress(String destinationAddress) {
    this.destinationAddress = destinationAddress;
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

  public String getSourceAddress() {
    return sourceAddress;
  }

  public String getDestinationAddress() {
    return destinationAddress;
  }

  public DeliveryMode getDeliveryMode() {
    return deliveryMode;
  }
}
