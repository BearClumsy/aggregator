package dplatonov.portal.service;

import dplatonov.portal.payload.UserPayload;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public UserPayload createUser(UserPayload user) {
        user.setRole("user");
        return null;
    }
}
