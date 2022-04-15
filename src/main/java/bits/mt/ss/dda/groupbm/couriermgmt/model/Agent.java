package bits.mt.ss.dda.groupbm.couriermgmt.model;

public class Agent {

  private final long contactNumber;
  private final String name;
  private final String addressLine;
  private final long pincode;
  private final String city;
  private final String state;
  private final Branch branch;

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
