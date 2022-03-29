package bits.mt.ss.dda.groupbm.couriermgmt.model;

public class PincodeBranch {
  private final long pincode;
  private final String branchCode;

  public PincodeBranch(long pincode, String branchCode) {
    this.pincode = pincode;
    this.branchCode = branchCode;
  }

  public long getPincode() {
    return pincode;
  }

  public String getBranchCode() {
    return branchCode;
  }
}
