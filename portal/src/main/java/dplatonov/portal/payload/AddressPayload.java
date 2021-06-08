package dplatonov.portal.payload;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class AddressPayload {
  private Long id;

  @NotEmpty(message = "City cannot be empty or null")
  private String city;

  @NotEmpty(message = "Address cannot be empty or null")
  private String address;
}
