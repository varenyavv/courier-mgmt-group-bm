package bits.mt.ss.dda.groupbm.couriermgmt.model.response;

import bits.mt.ss.dda.groupbm.couriermgmt.model.Branch;

public class ForwardShipmentResponse {

  private String consignmentNumber;
  private String status;
  private String statusRemarks;
  private Branch nextHop;
  private String modeOfTransport;
  private String receiveDateTime;

  public String getConsignmentNumber() {
    return consignmentNumber;
  }

  public ForwardShipmentResponse setConsignmentNumber(String consignmentNumber) {
    this.consignmentNumber = consignmentNumber;
    return this;
  }

  public String getStatus() {
    return status;
  }

  public ForwardShipmentResponse setStatus(String status) {
    this.status = status;
    return this;
  }

  public String getStatusRemarks() {
    return statusRemarks;
  }

  public ForwardShipmentResponse setStatusRemarks(String statusRemarks) {
    this.statusRemarks = statusRemarks;
    return this;
  }

  public Branch getNextHop() {
    return nextHop;
  }

  public ForwardShipmentResponse setNextHop(Branch nextHop) {
    this.nextHop = nextHop;
    return this;
  }

  public String getModeOfTransport() {
    return modeOfTransport;
  }

  public ForwardShipmentResponse setModeOfTransport(String modeOfTransport) {
    this.modeOfTransport = modeOfTransport;
    return this;
  }

  public String getReceiveDateTime() {
    return receiveDateTime;
  }

  public ForwardShipmentResponse setReceiveDateTime(String receiveDateTime) {
    this.receiveDateTime = receiveDateTime;
    return this;
  }
}
