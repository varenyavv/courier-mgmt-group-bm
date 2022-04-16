package bits.mt.ss.dda.groupbm.couriermgmt.model.response;

public class BookShipmentResponse {

  private long shipmentId;
  private String consignmentNumber;
  private String bookingDateTime;

  public long getShipmentId() {
    return shipmentId;
  }

  public void setShipmentId(long shipmentId) {
    this.shipmentId = shipmentId;
  }

  public String getConsignmentNumber() {
    return consignmentNumber;
  }

  public void setConsignmentNumber(String consignmentNumber) {
    this.consignmentNumber = consignmentNumber;
  }

  public String getBookingDateTime() {
    return bookingDateTime;
  }

  public void setBookingDateTime(String bookingDateTime) {
    this.bookingDateTime = bookingDateTime;
  }
}
