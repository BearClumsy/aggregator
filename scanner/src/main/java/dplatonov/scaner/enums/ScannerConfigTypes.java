package dplatonov.scaner.enums;

import java.util.HashMap;
import java.util.Map;

public enum ScannerConfigTypes {
  CLASS("class"),
  NAME("name"),
  ID("id"),
  TYPE("type"),
  HREF("href"),

  TAG("tag"),
  XPATH("xpath");

  private final String click;

  private final static Map<String, ScannerConfigTypes> map = new HashMap<>();
  static {
    ScannerConfigTypes[] values = ScannerConfigTypes.values();
    for (ScannerConfigTypes type : values) {
      map.put(type.toString(), type);
    }
  }

  public static ScannerConfigTypes forValue(String value) {
    return map.get(value);
  }

  ScannerConfigTypes(String click) {
    this.click = click;
  }

  @Override
  public String toString() {
    return this.click;
  }
}
