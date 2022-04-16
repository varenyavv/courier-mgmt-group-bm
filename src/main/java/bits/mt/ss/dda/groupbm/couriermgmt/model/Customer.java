package bits.mt.ss.dda.groupbm.couriermgmt.model;

public class Customer {

  private final long contactNumber;
  private String name;
  private String addressLine;
  private long pincode;
  private String city;
  private String state;

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

  public void setName(String name) {
    this.name = name;
  }

  public void setAddressLine(String addressLine) {
    this.addressLine = addressLine;
  }

  public void setPincode(long pincode) {
    this.pincode = pincode;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setState(String state) {
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
