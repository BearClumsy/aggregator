package dplatonov.portal.service;

import dplatonov.portal.dao.UserDao;
import dplatonov.portal.entity.User;
import dplatonov.portal.payload.UserPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserDao dao;

  public List<UserPayload> getUsers() {
    return dao.findAll().stream().map(UserService::mapUsersToPayloads).collect(Collectors.toList());
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
}
