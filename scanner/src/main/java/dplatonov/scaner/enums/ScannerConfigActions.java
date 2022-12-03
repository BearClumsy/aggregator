package dplatonov.scaner.enums;

public enum ScannerConfigActions {
  INSERT("insert"),
  CLICK("click"),
  SCAN("scan"),
  REPEAT("repeat"),
  PAUSE("pause");

  private final String click;

  ScannerConfigActions(String click) {
    this.click = click;
  }

  @Override
  public String toString() {
    return this.click;
  }
}
