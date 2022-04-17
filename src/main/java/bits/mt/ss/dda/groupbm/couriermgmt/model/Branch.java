package bits.mt.ss.dda.groupbm.couriermgmt.model;

public class Branch {

  private String branchCode;
  private String branchName;
  private String addressLine;
  private long pincode;
  private String city;
  private String state;

  public Branch() {}

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

  public void setBranchCode(String branchCode) {
    this.branchCode = branchCode;
  }

  public String getBranchName() {
    return branchName;
  }

  public void setBranchName(String branchName) {
    this.branchName = branchName;
  }

  public String getAddressLine() {
    return addressLine;
  }

  public void setAddressLine(String addressLine) {
    this.addressLine = addressLine;
  }

  public long getPincode() {
    return pincode;
  }

  public void setPincode(long pincode) {
    this.pincode = pincode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
