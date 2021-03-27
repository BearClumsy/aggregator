package dplatonov.portal.payload;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserPayload {
    private Long id;
    private String name;
    private String secondName;
    private String role;
    private String email;
    private String password;
}
