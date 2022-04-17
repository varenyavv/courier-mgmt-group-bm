package bits.mt.ss.dda.groupbm.couriermgmt.model.response;

import java.util.List;

public class TrackShipmentResponse {

  private String consignmentNumber;
  private long sourcePincode;
  private String destinationAddress;
  private long distanceInKm;
  private double bookingAmount;
  private String status;
  List<ShipmentHistoryRecord> shipmentHistory;

  public String getConsignmentNumber() {
    return consignmentNumber;
  }

  public TrackShipmentResponse setConsignmentNumber(String consignmentNumber) {
    this.consignmentNumber = consignmentNumber;
    return this;
  }

  public long getSourcePincode() {
    return sourcePincode;
  }

  public TrackShipmentResponse setSourcePincode(long sourcePincode) {
    this.sourcePincode = sourcePincode;
    return this;
  }

  public String getDestinationAddress() {
    return destinationAddress;
  }

  public TrackShipmentResponse setDestinationAddress(
      String destAddressLine, String destCity, long destPincode, String destState) {
    this.destinationAddress =
        destAddressLine + ", " + destCity + " - " + destPincode + ", " + destState;
    return this;
  }

  public long getDistanceInKm() {
    return distanceInKm;
  }

  public TrackShipmentResponse setDistanceInKm(long distanceInKm) {
    this.distanceInKm = distanceInKm;
    return this;
  }

  public double getBookingAmount() {
    return bookingAmount;
  }

  public TrackShipmentResponse setBookingAmount(double bookingAmount) {
    this.bookingAmount = bookingAmount;
    return this;
  }

  public String getStatus() {
    return status;
  }

  public TrackShipmentResponse setStatus(String status) {
    this.status = status;
    return this;
  }

  public List<ShipmentHistoryRecord> getShipmentHistory() {
    return shipmentHistory;
  }

  public TrackShipmentResponse setShipmentHistory(List<ShipmentHistoryRecord> shipmentHistory) {
    this.shipmentHistory = shipmentHistory;
    return this;
  }
}
