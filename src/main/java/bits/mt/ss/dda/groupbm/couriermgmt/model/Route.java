package bits.mt.ss.dda.groupbm.couriermgmt.model;

import java.util.List;

public class Route {

  private final List<Hop> hops;
  private final double cost;
  private double totalDistance;

  public Route(List<Hop> hops, double cost) {
    this.hops = hops;
    this.cost = cost;
  }

  public List<Hop> getHops() {
    return hops;
  }

  public double getCost() {
    return cost;
  }

  public double getTotalDistance() {
    return totalDistance;
  }

  public Route setTotalDistance(double totalDistance) {
    this.totalDistance = totalDistance;
    return this;
  }

  @Override
  public String toString() {
    return "Allotted Route: "
        + hops.toString()
        + "\n Total Cost: "
        + cost
        + "\n Approx distance: "
        + totalDistance;
  }
}
