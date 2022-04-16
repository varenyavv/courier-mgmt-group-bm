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

  public long getShipmentId() {
    return shipmentId;
  }

  public Shipment setShipmentId(long shipmentId) {
    this.shipmentId = shipmentId;
    return this;
  }

  public String getConsignmentNumber() {
    return consignmentNumber;
  }

  public Shipment setConsignmentNumber(String consignmentNumber) {
    this.consignmentNumber = consignmentNumber;
    return this;
  }

  public long getSourcePincode() {
    return sourcePincode;
  }

  public Shipment setSourcePincode(long sourcePincode) {
    this.sourcePincode = sourcePincode;
    return this;
  }

  public long getDestPincode() {
    return destPincode;
  }

  public Shipment setDestPincode(long destPincode) {
    this.destPincode = destPincode;
    return this;
  }

  public int getLengthInCm() {
    return lengthInCm;
  }

  public Shipment setLengthInCm(int lengthInCm) {
    this.lengthInCm = lengthInCm;
    return this;
  }

  public int getWidthInCm() {
    return widthInCm;
  }

  public Shipment setWidthInCm(int widthInCm) {
    this.widthInCm = widthInCm;
    return this;
  }

  public int getHeightInCm() {
    return heightInCm;
  }

  public Shipment setHeightInCm(int heightInCm) {
    this.heightInCm = heightInCm;
    return this;
  }

  public int getWeightInGram() {
    return weightInGram;
  }

  public Shipment setWeightInGram(int weightInGram) {
    this.weightInGram = weightInGram;
    return this;
  }

  public String getDestAddressLine() {
    return destAddressLine;
  }

  public Shipment setDestAddressLine(String destAddressLine) {
    this.destAddressLine = destAddressLine;
    return this;
  }

  public String getDestCity() {
    return destCity;
  }

  public Shipment setDestCity(String destCity) {
    this.destCity = destCity;
    return this;
  }

  public String getDestState() {
    return destState;
  }

  public Shipment setDestState(String destState) {
    this.destState = destState;
    return this;
  }

  public long getDistanceInKm() {
    return distanceInKm;
  }

  public Shipment setDistanceInKm(long distanceInKm) {
    this.distanceInKm = distanceInKm;
    return this;
  }

  public double getBookingAmount() {
    return bookingAmount;
  }

  public Shipment setBookingAmount(double bookingAmount) {
    this.bookingAmount = bookingAmount;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public Shipment setStatus(Status status) {
    this.status = status;
    return this;
  }

  public Customer getCustomer() {
    return customer;
  }

  public Shipment setCustomer(Customer customer) {
    this.customer = customer;
    return this;
  }
}
