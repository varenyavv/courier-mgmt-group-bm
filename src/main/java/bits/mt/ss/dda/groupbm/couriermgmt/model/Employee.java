package bits.mt.ss.dda.groupbm.couriermgmt.model;

public class Employee {

  private long employeeId;
  private long contactNumber;
  private String name;
  private String addressLine;
  private long pincode;
  private String city;
  private String state;
  private Branch branch;

  public Employee() {}

  public Employee(
      long employeeId,
      long contactNumber,
      String name,
      String addressLine,
      long pincode,
      String city,
      String state,
      Branch branch) {
    this.employeeId = employeeId;
    this.contactNumber = contactNumber;
    this.name = name;
    this.addressLine = addressLine;
    this.pincode = pincode;
    this.city = city;
    this.state = state;
    this.branch = branch;
  }

  public Employee setEmployeeId(long employeeId) {
    this.employeeId = employeeId;
    return this;
  }

  public Employee setContactNumber(long contactNumber) {
    this.contactNumber = contactNumber;
    return this;
  }

  public Employee setName(String name) {
    this.name = name;
    return this;
  }

  public Employee setAddressLine(String addressLine) {
    this.addressLine = addressLine;
    return this;
  }

  public Employee setPincode(long pincode) {
    this.pincode = pincode;
    return this;
  }

  public Employee setCity(String city) {
    this.city = city;
    return this;
  }

  public Employee setState(String state) {
    this.state = state;
    return this;
  }

  public Employee setBranch(Branch branch) {
    this.branch = branch;
    return this;
  }

  public long getEmployeeId() {
    return employeeId;
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
