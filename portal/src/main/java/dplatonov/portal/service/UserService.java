package dplatonov.portal.service;

import dplatonov.portal.dao.UserDao;
import dplatonov.portal.entity.User;
import dplatonov.portal.payload.UserPayload;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
  private static final Logger log = LoggerFactory.getLogger(UserService.class);
  private final UserDao dao;

  public List<UserPayload> getUsers() {
    return dao.findAll().stream().map(UserService::mapUsersToPayloads).collect(Collectors.toList());
  }

  public UserPayload getUserByEmail(String email) {
    return mapUsersToPayloads(dao.findByEmail(email));
  }

  public UserPayload createUser(UserPayload userPayload) {
    User newUser = mapUserPayloadToUser(userPayload);
    Optional<User> existUserOptional = dao.findByName(newUser);
    if (existUserOptional.isPresent()) {
      User existUser = existUserOptional.get();
      log.warn(
          "Current User with name "
              + existUser.getName()
              + " and email "
              + existUser.getEmail()
              + " is exist");
      return null;
    }
    User savedUser = dao.save(newUser);
    return mapUsersToPayloads(savedUser);
  }

  private static UserPayload mapUsersToPayloads(User user) {
    return UserPayload.builder()
        .id(user.getId())
        .name(user.getName())
        .secondName(user.getSecondName())
        .email(user.getEmail())
        .role(user.getRole().getRole())
        .build();
  }

  private User mapUserPayloadToUser(UserPayload userPayload) {
    return User.builder()
        .name(userPayload.getName())
        .secondName(userPayload.getSecondName())
        .email(userPayload.getEmail())
        .password(userPayload.getPassword())
        .build();
  }
}
