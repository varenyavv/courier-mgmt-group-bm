package bits.mt.ss.dda.groupbm.couriermgmt.model;

import java.util.List;

public class Route {

  private final List<Hop> hops;
  private final Double cost;
  private Long totalDistance;
  private final boolean serviceable;

  public Route(List<Hop> hops, Double cost) {
    this.hops = hops;
    this.cost = cost;
    this.serviceable = true;
  }

  public List<Hop> getHops() {
    return hops;
  }

  public Double getCost() {
    return cost;
  }

  public Long getTotalDistance() {
    return totalDistance;
  }

  public Route setTotalDistance(long totalDistance) {
    this.totalDistance = totalDistance;
    return this;
  }

  public boolean isServiceable() {
    return serviceable;
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
