package dplatonov.portal.payload;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Builder
@Data
public class UserPayload {
  private Long id;

  @NotEmpty(message = "Name cannot be empty")
  private String name;

  @NotEmpty(message = "Second name cannot be empty")
  private String secondName;

  @NotEmpty(message = "Role name cannot be empty")
  private String role;

  @NotEmpty(message = "Email name cannot be empty")
  private String email;

  @NotEmpty(message = "Password name cannot be empty")
  private String password;
}
