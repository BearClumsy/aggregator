package dplatonov.scaner.messages;

import dplatonov.scaner.enums.MsgStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Action {

  private final Long scannerId;
  private final MsgStatus status;

}
