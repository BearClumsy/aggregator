package dplatonov.portal.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressPayload {
  public Long id;
  public String city;
  public String address;
}
