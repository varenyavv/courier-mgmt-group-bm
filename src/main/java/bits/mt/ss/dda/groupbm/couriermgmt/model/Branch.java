package bits.mt.ss.dda.groupbm.couriermgmt.model;

public class Branch {

  private final String branchCode;
  private final String branchName;
  private final String addressLine;
  private final long pincode;
  private final String city;
  private final String state;

  public Branch(
      String branchCode,
      String branchName,
      String addressLine,
      long pincode,
      String city,
      String state) {
    this.branchCode = branchCode;
    this.branchName = branchName;
    this.addressLine = addressLine;
    this.pincode = pincode;
    this.city = city;
    this.state = state;
  }

  public String getBranchCode() {
    return branchCode;
  }

  public String getBranchName() {
    return branchName;
  }

  public String getAddressLine() {
    return addressLine;
  }

  public long getPincode() {
    return pincode;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }
}
