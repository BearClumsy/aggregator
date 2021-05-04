package dplatonov.portal.service;

import dplatonov.portal.dao.UserDao;
import dplatonov.portal.entity.Role;
import dplatonov.portal.entity.User;
import dplatonov.portal.payload.UserPayload;
import dplatonov.portal.validate.UserValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
  private static final Logger log = LoggerFactory.getLogger(UserService.class);
  private final UserDao dao;
  private final UserValidator userValidator;
  private final BCryptPasswordEncoder encoder;

  public List<UserPayload> getUsers() {
    return dao.findAll().stream().map(UserService::mapUsersToPayloads).collect(Collectors.toList());
  }

  public UserPayload getUserByEmail(String email) {
    return mapUsersToPayloads(dao.findByEmail(email));
  }

  public UserPayload createUser(UserPayload userPayload) {
    Optional<Role> optionalRole = userValidator.validateNewUserRole(userPayload.getRole());
    Optional<User> existOptionalUser = userValidator.validateNewUserByName(userPayload.getLogin());
    if (existOptionalUser.isPresent() || optionalRole.isEmpty()) {
      return null;
    }

    userPayload.setPassword(encoder.encode(userPayload.getPassword()));
    User newUser = mapUserPayloadToUser(userPayload, optionalRole.get());
    newUser.setActive(true);

    User savedUser = dao.save(newUser);
    log.info("USER-SERVICE-001: User is created success");
    return mapUsersToPayloads(savedUser);
  }

  private static UserPayload mapUsersToPayloads(User user) {
    return UserPayload.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .secondName(user.getSecondName())
        .email(user.getEmail())
        .role(user.getRole().getRole())
        .login(user.getLogin())
        .build();
  }

  private User mapUserPayloadToUser(UserPayload userPayload, Role role) {
    return User.builder()
        .firstName(userPayload.getFirstName())
        .secondName(userPayload.getSecondName())
        .email(userPayload.getEmail())
        .password(userPayload.getPassword())
        .login(userPayload.getLogin())
        .role(role)
        .build();
  }
}
