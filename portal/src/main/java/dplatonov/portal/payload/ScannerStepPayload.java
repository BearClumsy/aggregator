package dplatonov.portal.payload;

import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScannerStepPayload {
  @NotEmpty(message = "Tag cannot be empty or null")
  private final String tag;
  @NotEmpty(message = "Type cannot be empty or null")
  private final String type;
  @NotEmpty(message = "Action cannot be empty or null")
  private final String action;
  private final String value;
  @NotEmpty(message = "Active cannot be empty or null")
  private final boolean active;
}
