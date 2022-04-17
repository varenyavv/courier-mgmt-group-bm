package bits.mt.ss.dda.groupbm.couriermgmt.model.response;

public class DeliverShipmentResponse {

  private String consignmentNumber;
  private String status;
  private String statusRemarks;
  private String deliveryDateTime;

  public String getConsignmentNumber() {
    return consignmentNumber;
  }

  public DeliverShipmentResponse setConsignmentNumber(String consignmentNumber) {
    this.consignmentNumber = consignmentNumber;
    return this;
  }

  public String getStatus() {
    return status;
  }

  public DeliverShipmentResponse setStatus(String status) {
    this.status = status;
    return this;
  }

  public String getStatusRemarks() {
    return statusRemarks;
  }

  public DeliverShipmentResponse setStatusRemarks(String statusRemarks) {
    this.statusRemarks = statusRemarks;
    return this;
  }

  public String getDeliveryDateTime() {
    return deliveryDateTime;
  }

  public DeliverShipmentResponse setDeliveryDateTime(String deliveryDateTime) {
    this.deliveryDateTime = deliveryDateTime;
    return this;
  }
}
