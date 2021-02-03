package dplatonov.portal.payload;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyPayload {
  private Long id;
  private String name;
  private String city;
  private String description;
  private List<AddressPayload> addresses;
}
