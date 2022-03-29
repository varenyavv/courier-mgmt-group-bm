package bits.mt.ss.dda.groupbm.couriermgmt.model;

import bits.mt.ss.dda.groupbm.couriermgmt.enums.TransportMode;

public class Hop {

  Branch branch;
  TransportMode shipVia;

  public Hop(Branch branch, TransportMode shipVia) {
    this.branch = branch;
    this.shipVia = shipVia;
  }

  public Branch getBranch() {
    return branch;
  }

  public TransportMode getShipVia() {
    return shipVia;
  }

  @Override
  public String toString() {
    if (null == shipVia) {
      return branch.getBranchCode() + " - " + branch.getBranchName();
    }
    return branch.getBranchCode() + " - " + branch.getBranchName() + " via " + shipVia + " to ";
  }
}
