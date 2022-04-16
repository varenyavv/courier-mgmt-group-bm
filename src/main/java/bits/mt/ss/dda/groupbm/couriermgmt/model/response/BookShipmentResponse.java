package bits.mt.ss.dda.groupbm.couriermgmt.model.response;

public class BookShipmentResponse {

  private long shipmentId;
  private String consignmentNumber;
  private String bookingDateTime;

  public long getShipmentId() {
    return shipmentId;
  }

  public BookShipmentResponse setShipmentId(long shipmentId) {
    this.shipmentId = shipmentId;
    return this;
  }

  public String getConsignmentNumber() {
    return consignmentNumber;
  }

  public BookShipmentResponse setConsignmentNumber(String consignmentNumber) {
    this.consignmentNumber = consignmentNumber;
    return this;
  }

  public String getBookingDateTime() {
    return bookingDateTime;
  }

  public BookShipmentResponse setBookingDateTime(String bookingDateTime) {
    this.bookingDateTime = bookingDateTime;
    return this;
  }
}
