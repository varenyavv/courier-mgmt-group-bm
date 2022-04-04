package bits.mt.ss.dda.groupbm.couriermgmt.model;

import bits.mt.ss.dda.groupbm.couriermgmt.enums.TransportMode;

public class Hop {

  Branch branch;
  TransportMode shipVia;
  int hopCounter;

  public Hop(Branch branch, TransportMode shipVia, int hopCounter) {
    this.branch = branch;
    this.shipVia = shipVia;
    this.hopCounter = hopCounter;
  }

  public Branch getBranch() {
    return branch;
  }

  public TransportMode getShipVia() {
    return shipVia;
  }

  public int getHopCounter() {
    return hopCounter;
  }

  @Override
  public String toString() {
    if (null == shipVia) {
      return branch.getBranchCode() + " - " + branch.getBranchName();
    }
    return branch.getBranchCode() + " - " + branch.getBranchName() + " via " + shipVia + " to ";
  }
}
