package dplatonov.scaner.messages;

import dplatonov.scaner.enums.MsgStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Action {

  private Long scannerId;
  private MsgStatus status;

}
