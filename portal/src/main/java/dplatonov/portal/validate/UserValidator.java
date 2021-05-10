package dplatonov.portal.validate;

import dplatonov.portal.dao.RoleDao;
import dplatonov.portal.dao.UserDao;
import dplatonov.portal.entity.Role;
import dplatonov.portal.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator {
  private static final Logger log = LoggerFactory.getLogger(UserValidator.class);
  private final UserDao userDao;
  private final RoleDao roleDao;

  public Optional<User> validateByLogin(String login) {
    Optional<User> existUserOptional = userDao.findByLogin(login);
    if (existUserOptional.isEmpty()) {
      log.warn("USER-VALIDATOR-001: User with login " + login + " is not exist");
    }

    return existUserOptional;
  }

  public Optional<Role> validateRole(String role) {
    Optional<Role> optionalRole = roleDao.findByRole(role);
    if (optionalRole.isEmpty()) {
      log.error("USER-VALIDATOR-002: Role is not correct!");
    }

    return optionalRole;
  }

  public Optional<User> validateById(Long id) {
    Optional<User> existUserOptional = userDao.findById(id);
    if (existUserOptional.isEmpty()) {
      log.error("USER-VALIDATOR-003: User with id " + id + " does not exist!");
    }

    return existUserOptional;
  }
}
