package bits.mt.ss.dda.groupbm.couriermgmt.model;

import java.time.LocalDateTime;

import bits.mt.ss.dda.groupbm.couriermgmt.enums.Status;
import bits.mt.ss.dda.groupbm.couriermgmt.enums.TransportMode;

public class ShipmentTracker {

  private Shipment shipment;
  private Branch currentBranch;
  private Branch nexBranch;
  private TransportMode transportMode;
  private Status status;
  private String statusRemarks;
  private LocalDateTime creationDateTime;
  private Employee employee;
  private Agent agent;

  public Shipment getShipment() {
    return shipment;
  }

  public ShipmentTracker setShipment(Shipment shipment) {
    this.shipment = shipment;
    return this;
  }

  public Branch getCurrentBranch() {
    return currentBranch;
  }

  public ShipmentTracker setCurrentBranch(Branch currentBranch) {
    this.currentBranch = currentBranch;
    return this;
  }

  public Branch getNexBranch() {
    return nexBranch;
  }

  public ShipmentTracker setNexBranch(Branch nexBranch) {
    this.nexBranch = nexBranch;
    return this;
  }

  public TransportMode getTransportMode() {
    return transportMode;
  }

  public ShipmentTracker setTransportMode(TransportMode transportMode) {
    this.transportMode = transportMode;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public ShipmentTracker setStatus(Status status) {
    this.status = status;
    return this;
  }

  public String getStatusRemarks() {
    return statusRemarks;
  }

  public ShipmentTracker setStatusRemarks(String statusRemarks) {
    this.statusRemarks = statusRemarks;
    return this;
  }

  public LocalDateTime getCreationDateTime() {
    return creationDateTime;
  }

  public ShipmentTracker setCreationDateTime(LocalDateTime creationDateTime) {
    this.creationDateTime = creationDateTime;
    return this;
  }

  public Employee getEmployee() {
    return employee;
  }

  public ShipmentTracker setEmployee(Employee employee) {
    this.employee = employee;
    return this;
  }

  public Agent getAgent() {
    return agent;
  }

  public ShipmentTracker setAgent(Agent agent) {
    this.agent = agent;
    return this;
  }
}
