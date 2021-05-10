package dplatonov.portal.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressPayload {
  private Long id;
  private String city;
  private String address;
}
