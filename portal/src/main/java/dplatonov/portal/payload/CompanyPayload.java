package dplatonov.portal.payload;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CompanyPayload {
  private Long id;
  private String name;
  private String city;
  private String description;
  private List<AddressPayload> addresses;
}
