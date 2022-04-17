package bits.mt.ss.dda.groupbm.couriermgmt.model.response;

public class ShipmentHistoryRecord {

  private String currentBranch;
  private String nexBranch;
  private String transportMode;
  private String status;
  private String statusRemarks;
  private String creationDateTime;
  private String agentName;
  private Long agentContact;

  public String getCurrentBranch() {
    return currentBranch;
  }

  public ShipmentHistoryRecord setCurrentBranch(String currentBranch) {
    this.currentBranch = currentBranch;
    return this;
  }

  public String getNexBranch() {
    return nexBranch;
  }

  public ShipmentHistoryRecord setNexBranch(String nexBranch) {
    this.nexBranch = nexBranch;
    return this;
  }

  public String getTransportMode() {
    return transportMode;
  }

  public ShipmentHistoryRecord setTransportMode(String transportMode) {
    this.transportMode = transportMode;
    return this;
  }

  public String getStatus() {
    return status;
  }

  public ShipmentHistoryRecord setStatus(String status) {
    this.status = status;
    return this;
  }

  public String getStatusRemarks() {
    return statusRemarks;
  }

  public ShipmentHistoryRecord setStatusRemarks(String statusRemarks) {
    this.statusRemarks = statusRemarks;
    return this;
  }

  public String getCreationDateTime() {
    return creationDateTime;
  }

  public ShipmentHistoryRecord setCreationDateTime(String creationDateTime) {
    this.creationDateTime = creationDateTime;
    return this;
  }

  public String getAgentName() {
    return agentName;
  }

  public ShipmentHistoryRecord setAgentName(String agentName) {
    this.agentName = agentName;
    return this;
  }

  public Long getAgentContact() {
    return agentContact;
  }

  public ShipmentHistoryRecord setAgentContact(Long agentContact) {
    this.agentContact = agentContact;
    return this;
  }
}
