package bits.mt.ss.dda.groupbm.couriermgmt.model.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ForwardShipmentRequest {

  @JsonIgnore private String consignmentNumber;

  @NotBlank private String receivingBranchCode;

  private String statusRemarks;

  public String getConsignmentNumber() {
    return consignmentNumber;
  }

  public void setConsignmentNumber(String consignmentNumber) {
    this.consignmentNumber = consignmentNumber;
  }

  public String getReceivingBranchCode() {
    return receivingBranchCode;
  }

  public void setReceivingBranchCode(String receivingBranchCode) {
    this.receivingBranchCode = receivingBranchCode;
  }

  public String getStatusRemarks() {
    return statusRemarks;
  }

  public void setStatusRemarks(String statusRemarks) {
    this.statusRemarks = statusRemarks;
  }
}
