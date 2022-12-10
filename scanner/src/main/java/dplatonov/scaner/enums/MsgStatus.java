package dplatonov.scaner.enums;

import lombok.Getter;

public enum MsgStatus {
  START("start"),
  STOP("stop"),
  FINISH("finish");

  @Getter
  private final String status;

  MsgStatus(String status) {
    this.status = status;
  }
}
