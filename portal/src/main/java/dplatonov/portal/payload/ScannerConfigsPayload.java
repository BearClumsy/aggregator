package dplatonov.portal.payload;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScannerConfigsPayload {
  private final Long id;
  @NotEmpty(message = "Name cannot be empty or null")
  private final String name;
  @NotEmpty(message = "Url cannot be empty or null")
  private final String url;
  @NotEmpty(message = "ScannerStepPayload cannot be empty or null")
  private final List<ScannerStepPayload> scannerSteps;
  @NotEmpty(message = "Active cannot be empty or null")
  private final boolean active;
  @NotEmpty(message = "User id cannot be empty or null")
  private final Long userId;
  private final Boolean isStarted;
}
