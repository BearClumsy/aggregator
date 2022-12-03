package dplatonov.portal.service;

import dplatonov.portal.dao.UserDao;
import dplatonov.portal.entity.Role;
import dplatonov.portal.entity.User;
import dplatonov.portal.enums.RoleEnum;
import dplatonov.portal.payload.UserPayload;
import dplatonov.portal.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

  public User getUserById(Long id) {
    return dao.findById(id).orElse(User.builder().build());
  }

  public UserPayload createUser(UserPayload userPayload) {
    Optional<Role> optionalRole = userValidator.validateRole(userPayload.getRole());
    Optional<User> existOptionalUser = userValidator.validateByLogin(userPayload.getLogin());
    if (existOptionalUser.isPresent()
        || optionalRole.isEmpty()
        || optionalRole.get().getRole().equals(RoleEnum.getRoleEnum(RoleEnum.ADMIN))) {
      log.warn("USER-SERVICE-002: User with login " + userPayload.getLogin() + " can't be created");
      return null;
    }

    userPayload.setPassword(encoder.encode(userPayload.getPassword()));
    User newUser = mapUserPayloadToUser(userPayload);
    newUser.setRole(optionalRole.get());

    User savedUser = dao.save(newUser);
    log.info("USER-SERVICE-001: User is created success");
    return mapUsersToPayloads(savedUser);
  }

  public UserPayload updateUser(UserPayload updatableUser) {
    Long updatableUserId = updatableUser.getId();
    Optional<User> existOptionalUser = userValidator.validateById(updatableUserId);
    Optional<Role> role = userValidator.validateRole(updatableUser.getRole());
    if (existOptionalUser.isEmpty() || role.isEmpty()) {
      return null;
    }

    User user = mapUserPayloadToUser(updatableUser);
    user.setRole(role.get());
    user.setPassword(encoder.encode(updatableUser.getPassword()));
    User updatedUser = dao.save(user);
    log.info("USER-SERVICE-003: User with id " + updatableUserId + " updated success");

    return mapUsersToPayloads(updatedUser);
  }

  public void deleteUser(Long id) {
    Optional<User> existOptionalUser = userValidator.validateById(id);
    if (existOptionalUser.isEmpty() || !existOptionalUser.get().isActive()) {
      return;
    }

    User existUser = existOptionalUser.get();
    existUser.setActive(false);
    dao.save(existUser);
  }

  public boolean isAdmin() {
    User user = getCurentUser();
    Role role = user.getRole();
    return role.getRole().equals(RoleEnum.getRoleEnum(RoleEnum.ADMIN));
  }

  public User getCurentUser(){
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();

    return dao.findByEmail(email);
  }

  private static UserPayload mapUsersToPayloads(User user) {
    return UserPayload.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .secondName(user.getSecondName())
        .email(user.getEmail())
        .role(user.getRole().getRole())
        .login(user.getLogin())
        .active(user.isActive())
        .build();
  }

  private User mapUserPayloadToUser(UserPayload userPayload) {
    return User.builder()
        .id(userPayload.getId())
        .firstName(userPayload.getFirstName())
        .secondName(userPayload.getSecondName())
        .email(userPayload.getEmail())
        .password(userPayload.getPassword())
        .login(userPayload.getLogin())
        .active(userPayload.isActive())
        .build();
  }
}
