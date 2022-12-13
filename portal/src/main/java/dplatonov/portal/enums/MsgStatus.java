package dplatonov.portal.enums;

import lombok.Getter;

public enum MsgStatus {
  START("start"),
  STOP("stop"),
  FINISH("finish"),
  CRASH("crash");

  @Getter
  private final String status;

  MsgStatus(String status) {
    this.status = status;
  }
}
