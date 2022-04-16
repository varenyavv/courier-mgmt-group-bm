package bits.mt.ss.dda.groupbm.couriermgmt.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

public class BookShipmentRequest {

  private String sourceAddressLine;
  private String sourceCity;
  private String sourceState;
  private long sourcePincode;

  @Range(
      min = 6000000000L,
      max = 9999999999L,
      message = "Valid customer's contact number is required.")
  private long customerContactNum;

  private String customerName;
  @NotBlank private String destAddressLine;
  @NotBlank private String destCity;

  @Size(min = 2, max = 2, message = "Valid two characters' destination state code is required.")
  @NotBlank
  private String destState;

  @Range(min = 100000L, max = 999999L, message = "Valid destination pincode is required.")
  private long destPincode;

  @Range(min = 1, message = "Non-zero length in cm is required.")
  private int lengthInCm;

  @Range(min = 1, message = "Non-zero width in cm is required.")
  private int widthInCm;

  @Range(min = 1, message = "Non-zero height in cm is required.")
  private int heightInCm;

  @Range(min = 1, message = "Non-zero weight in gram is required.")
  private int weightInGram;

  @Range(min = 50, message = "Valid booking amount is required.")
  private double bookingAmount;

  public String getSourceAddressLine() {
    return sourceAddressLine;
  }

  public void setSourceAddressLine(String sourceAddressLine) {
    this.sourceAddressLine = sourceAddressLine;
  }

  public String getSourceCity() {
    return sourceCity;
  }

  public void setSourceCity(String sourceCity) {
    this.sourceCity = sourceCity;
  }

  public String getSourceState() {
    return sourceState;
  }

  public void setSourceState(String sourceState) {
    this.sourceState = sourceState;
  }

  public long getSourcePincode() {
    return sourcePincode;
  }

  public void setSourcePincode(long sourcePincode) {
    this.sourcePincode = sourcePincode;
  }

  public long getCustomerContactNum() {
    return customerContactNum;
  }

  public void setCustomerContactNum(long customerContactNum) {
    this.customerContactNum = customerContactNum;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getDestAddressLine() {
    return destAddressLine;
  }

  public void setDestAddressLine(String destAddressLine) {
    this.destAddressLine = destAddressLine;
  }

  public String getDestCity() {
    return destCity;
  }

  public void setDestCity(String destCity) {
    this.destCity = destCity;
  }

  public String getDestState() {
    return destState;
  }

  public void setDestState(String destState) {
    this.destState = destState;
  }

  public long getDestPincode() {
    return destPincode;
  }

  public void setDestPincode(long destPincode) {
    this.destPincode = destPincode;
  }

  public int getLengthInCm() {
    return lengthInCm;
  }

  public void setLengthInCm(int lengthInCm) {
    this.lengthInCm = lengthInCm;
  }

  public int getWidthInCm() {
    return widthInCm;
  }

  public void setWidthInCm(int widthInCm) {
    this.widthInCm = widthInCm;
  }

  public int getHeightInCm() {
    return heightInCm;
  }

  public void setHeightInCm(int heightInCm) {
    this.heightInCm = heightInCm;
  }

  public int getWeightInGram() {
    return weightInGram;
  }

  public void setWeightInGram(int weightInGram) {
    this.weightInGram = weightInGram;
  }

  public double getBookingAmount() {
    return bookingAmount;
  }

  public void setBookingAmount(double bookingAmount) {
    this.bookingAmount = bookingAmount;
  }
}
