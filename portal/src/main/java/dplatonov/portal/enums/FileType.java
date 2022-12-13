package dplatonov.portal.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

public enum FileType {
  CSV("csv"),
  XLSX("xlsx"),
  PDF("pdf");

  private static final Map<String, FileType> map = new HashMap<>();

  static {
    Arrays.stream(FileType.values()).forEach(value -> map.put(value.getType(), value));
  }

  @Getter
  private final String type;

  FileType(String type) {
    this.type = type;
  }

  public static FileType getType(String type) {
    return map.get(type);
  }
}
