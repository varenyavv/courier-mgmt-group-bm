package bits.mt.ss.dda.groupbm.couriermgmt.model;

public class Employee {

  private long employeeId;
  private long contactNumber;
  private String name;
  private Branch branch;

  public Employee() {}

  public Employee(long employeeId, long contactNumber, String name, Branch branch) {
    this.employeeId = employeeId;
    this.contactNumber = contactNumber;
    this.name = name;
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

  public Branch getBranch() {
    return branch;
  }
}
