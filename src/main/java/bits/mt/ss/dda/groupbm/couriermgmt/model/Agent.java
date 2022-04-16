package bits.mt.ss.dda.groupbm.couriermgmt.model;

public class Agent {

  private long contactNumber;
  private String name;
  private String addressLine;
  private long pincode;
  private String city;
  private String state;
  private Branch branch;

  public Agent() {}

  public Agent(
      long contactNumber,
      String name,
      String addressLine,
      long pincode,
      String city,
      String state,
      Branch branch) {
    this.contactNumber = contactNumber;
    this.name = name;
    this.addressLine = addressLine;
    this.pincode = pincode;
    this.city = city;
    this.state = state;
    this.branch = branch;
  }

  public Agent setContactNumber(long contactNumber) {
    this.contactNumber = contactNumber;
    return this;
  }

  public Agent setName(String name) {
    this.name = name;
    return this;
  }

  public Agent setAddressLine(String addressLine) {
    this.addressLine = addressLine;
    return this;
  }

  public Agent setPincode(long pincode) {
    this.pincode = pincode;
    return this;
  }

  public Agent setCity(String city) {
    this.city = city;
    return this;
  }

  public Agent setState(String state) {
    this.state = state;
    return this;
  }

  public Agent setBranch(Branch branch) {
    this.branch = branch;
    return this;
  }

  public long getContactNumber() {
    return contactNumber;
  }

  public String getName() {
    return name;
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

  public Branch getBranch() {
    return branch;
  }
}
