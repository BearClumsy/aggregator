package dplatonov.portal.enums;

import java.util.HashMap;
import java.util.Map;

public enum RoleEnum {
  ADMIN("admin"),
  PARTICIPANTS("participant");

  private final String role;
  private static final Map<String, RoleEnum> map = new HashMap<>();

  static {
    RoleEnum[] values = RoleEnum.values();
    for (RoleEnum value : values) {
      map.put(value.toString(), value);
    }
  }

  public static RoleEnum forValue(String role) {
    return map.get(role);
  }

  RoleEnum(String role) {
    this.role = role;
  }

  public static String getRoleEnum(RoleEnum type) {
    return type.toString();
  }

  @Override
  public String toString() {
    return this.role;
  }
}
