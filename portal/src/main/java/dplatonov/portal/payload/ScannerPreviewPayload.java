package dplatonov.portal.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ScannerPreviewPayload {

  private final Long id;
  private final Long scannerId;

  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private final Date startDate;
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private final Date finishDate;
  private final String value;

}
