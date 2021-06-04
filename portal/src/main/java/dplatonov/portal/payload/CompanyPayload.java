package dplatonov.portal.payload;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
public class CompanyPayload {
  private Long id;

  @NotEmpty(message = "Name cannot be empty or null")
  private String name;

  @NotEmpty(message = "City cannot be empty or null")
  private String city;

  @NotEmpty(message = "Description cannot be empty or null")
  private String description;

  @NotEmpty(message = "Addresses cannot be empty or null")
  private List<AddressPayload> addresses;

  @NotEmpty(message = "Active cannot be empty or null")
  private boolean active;
}
