package bits.mt.ss.dda.groupbm.couriermgmt.model;

public class Customer {

  private final long contactNumber;
  private final String name;
  private final String addressLine;
  private final long pincode;
  private final String city;
  private final String state;

  public Customer(
      long contactNumber,
      String name,
      String addressLine,
      long pincode,
      String city,
      String state) {
    this.contactNumber = contactNumber;
    this.name = name;
    this.addressLine = addressLine;
    this.pincode = pincode;
    this.city = city;
    this.state = state;
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
}
