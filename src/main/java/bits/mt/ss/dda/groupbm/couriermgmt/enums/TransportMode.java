package bits.mt.ss.dda.groupbm.couriermgmt.enums;

import java.util.Arrays;

public enum TransportMode {
  AIR(1, "Airplane"),
  RAILWAY(2, "Railway"),
  ROAD(3, "Road");

  private final Integer key;
  private final String value;

  TransportMode(Integer key, String value) {
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

  public static TransportMode getByValue(String value) {
    return Arrays.stream(TransportMode.values())
        .filter(e -> e.getValue().equals(value))
        .findFirst()
        .orElse(null);
  }
}
