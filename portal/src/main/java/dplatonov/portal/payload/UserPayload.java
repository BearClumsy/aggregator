package dplatonov.portal.payload;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Builder
@Data
public class UserPayload {
  private Long id;

  @NotEmpty(message = "Name cannot be empty or null")
  private String firstName;

  @NotEmpty(message = "Second cannot be empty or null")
  private String secondName;

  @NotEmpty(message = "Email cannot be empty or null")
  private String email;

  @NotEmpty(message = "Password cannot be empty or null")
  private String password;

  @NotEmpty(message = "Login cannot be empty or null")
  private String login;

  @NotEmpty(message = "Role cannot be empty or null")
  private String role;

  private boolean active;
}
