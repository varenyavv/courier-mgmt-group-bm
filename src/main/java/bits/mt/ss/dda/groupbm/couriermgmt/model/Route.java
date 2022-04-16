package bits.mt.ss.dda.groupbm.couriermgmt.model;

import java.util.List;

public class Route {

  private final List<Hop> hops;
  private final Double cost;
  private Distance distance;
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

  public Distance getDistance() {
    return distance;
  }

  public Route setDistance(Distance distance) {
    this.distance = distance;
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
        + distance;
  }
}
