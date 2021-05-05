package dplatonov.portal.enums;

public enum AccessConstants {
  ADMIN("admin"),
  PARTICIPANT("participant");
  private final String name;

  AccessConstants(String name) {
    this.name = name;
  }

  public static String getAccessEnum(AccessConstants type) {
    return type.toString();
  }

  @Override
  public String toString() {
    return this.name;
  }
}
