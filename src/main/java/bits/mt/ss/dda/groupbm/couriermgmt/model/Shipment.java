package bits.mt.ss.dda.groupbm.couriermgmt.model;

import bits.mt.ss.dda.groupbm.couriermgmt.enums.Status;

public class Shipment {

  private long shipmentId;
  private String consignmentNumber;
  private long sourcePincode;
  private long destPincode;
  private int lengthInCm;
  private int widthInCm;
  private int heightInCm;
  private int weightInGram;
  private String destAddressLine;
  private String destCity;
  private String destState;
  private long distanceInKm;
  private double bookingAmount;
  private Status status;
  private Customer customer;

  public Shipment setSourcePincode(long sourcePincode) {
    this.sourcePincode = sourcePincode;
    return this;
  }

  public Shipment setDestPincode(long destPincode) {
    this.destPincode = destPincode;
    return this;
  }

  public Shipment setLengthInCm(int lengthInCm) {
    this.lengthInCm = lengthInCm;
    return this;
  }

  public Shipment setWidthInCm(int widthInCm) {
    this.widthInCm = widthInCm;
    return this;
  }

  public Shipment setHeightInCm(int heightInCm) {
    this.heightInCm = heightInCm;
    return this;
  }

  public Shipment setWeightInGram(int weightInGram) {
    this.weightInGram = weightInGram;
    return this;
  }

  public long getSourcePincode() {
    return sourcePincode;
  }

  public long getDestPincode() {
    return destPincode;
  }

  public int getLengthInCm() {
    return lengthInCm;
  }

  public int getWidthInCm() {
    return widthInCm;
  }

  public int getHeightInCm() {
    return heightInCm;
  }

  public int getWeightInGram() {
    return weightInGram;
  }

  public Shipment setDestAddressLine(String destAddressLine) {
    this.destAddressLine = destAddressLine;
    return this;
  }

  public Shipment setDestCity(String destCity) {
    this.destCity = destCity;
    return this;
  }

  public Shipment setDestState(String destState) {
    this.destState = destState;
    return this;
  }

  public Shipment setDistanceInKm(long distanceInKm) {
    this.distanceInKm = distanceInKm;
    return this;
  }

  public Shipment setBookingAmount(double bookingAmount) {
    this.bookingAmount = bookingAmount;
    return this;
  }

  public Shipment setStatus(Status status) {
    this.status = status;
    return this;
  }

  public Shipment setCustomer(Customer customer) {
    this.customer = customer;
    return this;
  }

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

  public String getDestAddressLine() {
    return destAddressLine;
  }

  public String getDestCity() {
    return destCity;
  }

  public String getDestState() {
    return destState;
  }

  public long getDistanceInKm() {
    return distanceInKm;
  }

  public double getBookingAmount() {
    return bookingAmount;
  }

  public Status getStatus() {
    return status;
  }

  public Customer getCustomer() {
    return customer;
  }
}
