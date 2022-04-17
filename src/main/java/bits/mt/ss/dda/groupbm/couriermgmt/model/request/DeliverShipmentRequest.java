package bits.mt.ss.dda.groupbm.couriermgmt.model.request;

import bits.mt.ss.dda.groupbm.couriermgmt.validator.ValidateString;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DeliverShipmentRequest {

  @JsonIgnore private String consignmentNumber;

  @ValidateString(
      acceptedValues = {"Out for Delivery", "Delivered", "Undelivered"},
      message = "Allowed values are ['Out for Delivery', 'Delivered', 'Undelivered']")
  private String status;

  private String statusRemarks;

  public String getConsignmentNumber() {
    return consignmentNumber;
  }

  public void setConsignmentNumber(String consignmentNumber) {
    this.consignmentNumber = consignmentNumber;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatusRemarks() {
    return statusRemarks;
  }

  public void setStatusRemarks(String statusRemarks) {
    this.statusRemarks = statusRemarks;
  }
}
