package dplatonov.portal.payload;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Builder
@Data
public class UserPayload {
  private Long id;

  @NotEmpty(message = "Name cannot be empty")
  private String firstName;

  @NotEmpty(message = "Second cannot be empty")
  private String secondName;

  @NotEmpty(message = "Email cannot be empty")
  private String email;

  @NotEmpty(message = "Password cannot be empty")
  private String password;

  @NotEmpty(message = "Login connot be empty")
  private String login;

  @NotEmpty(message = "Role cannot be empty")
  private String role;
}
