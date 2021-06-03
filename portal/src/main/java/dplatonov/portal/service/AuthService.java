package dplatonov.portal.service;

import dplatonov.portal.payload.UserPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;

    public UserPayload createUser(UserPayload user) {
        user.setRole("participant");
        user.setActive(true);
        return userService.createUser(user);
    }
}
