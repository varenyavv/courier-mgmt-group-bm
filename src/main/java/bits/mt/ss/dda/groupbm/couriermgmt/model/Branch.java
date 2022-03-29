package bits.mt.ss.dda.groupbm.couriermgmt.model;

public class Branch {

  private final String branchCode;
  private final String branchName;
  private final String branchAddress;

  public Branch(String branchCode, String branchName, String branchAddress) {
    this.branchCode = branchCode;
    this.branchName = branchName;
    this.branchAddress = branchAddress;
  }

  public String getBranchCode() {
    return branchCode;
  }

  public String getBranchName() {
    return branchName;
  }

  public String getBranchAddress() {
    return branchAddress;
  }
}
