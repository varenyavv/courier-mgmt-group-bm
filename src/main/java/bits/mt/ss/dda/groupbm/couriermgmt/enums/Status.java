package bits.mt.ss.dda.groupbm.couriermgmt.enums;

import java.util.Arrays;

public enum Status {
  BOOKED(1, "Booked"),
  RECEIVED_AT_SOURCE_BRANCH(2, "Received at sender's home branch"),
  IN_TRANSIT(3, "In Transit"),
  RECEIVED_AT_DEST_BRANCH(4, "Received at recipient's home branch"),
  OUT_FOR_DELIVERY(5, "Out for Delivery"),
  DELIVERED(6, "Delivered"),
  UNDELIVERED(7, "Undelivered");

  private final Integer key;
  private final String value;

  Status(Integer key, String value) {
    this.key = key;
    this.value = value;
  }

  public Integer getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return this.value;
  }

  public static Status getByValue(String value) {
    return Arrays.stream(Status.values())
        .filter(e -> e.getValue().equals(value))
        .findFirst()
        .orElse(null);
  }
}
