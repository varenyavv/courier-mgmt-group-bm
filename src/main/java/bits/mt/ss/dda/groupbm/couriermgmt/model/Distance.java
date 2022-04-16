package bits.mt.ss.dda.groupbm.couriermgmt.model;

public class Distance {

  long sourcePincode;
  long destinationPincode;
  String sourceBranchCode;
  String destinationBranchCode;
  long distanceInKm;

  public Distance(
      long sourcePincode,
      long destinationPincode,
      String sourceBranchCode,
      String destinationBranchCode,
      long distanceInKm) {
    this.sourcePincode = sourcePincode;
    this.destinationPincode = destinationPincode;
    this.sourceBranchCode = sourceBranchCode;
    this.destinationBranchCode = destinationBranchCode;
    this.distanceInKm = distanceInKm;
  }

  public long getSourcePincode() {
    return sourcePincode;
  }

  public long getDestinationPincode() {
    return destinationPincode;
  }

  public String getSourceBranchCode() {
    return sourceBranchCode;
  }

  public String getDestinationBranchCode() {
    return destinationBranchCode;
  }

  public long getDistanceInKm() {
    return distanceInKm;
  }

  @Override
  public String toString() {
    return "Distance{"
        + "sourcePincode="
        + sourcePincode
        + ", destinationPincode="
        + destinationPincode
        + ", sourceBranchCode='"
        + sourceBranchCode
        + '\''
        + ", destinationBranchCode='"
        + destinationBranchCode
        + '\''
        + ", distanceInKm="
        + distanceInKm
        + '}';
  }
}
