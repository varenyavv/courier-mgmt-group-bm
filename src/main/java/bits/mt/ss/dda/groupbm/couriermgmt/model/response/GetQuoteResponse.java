package bits.mt.ss.dda.groupbm.couriermgmt.model.response;

public class GetQuoteResponse {

  private final Long totalDistance;
  private final Double cost;
  private final boolean serviceable;

  public GetQuoteResponse(Long totalDistance, Double cost) {
    this.totalDistance = totalDistance;
    this.cost = cost;
    this.serviceable = true;
  }

  public Double getCost() {
    return cost;
  }

  public Long getTotalDistance() {
    return totalDistance;
  }

  public boolean isServiceable() {
    return serviceable;
  }

  @Override
  public String toString() {
    return "Total Cost: " + cost + "\n Approx distance: " + totalDistance;
  }
}
