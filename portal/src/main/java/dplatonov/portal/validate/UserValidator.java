package dplatonov.portal.validate;

import dplatonov.portal.dao.RoleDao;
import dplatonov.portal.dao.UserDao;
import dplatonov.portal.entity.Role;
import dplatonov.portal.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserValidator {
  private static final Logger log = LoggerFactory.getLogger(UserValidator.class);
  private final UserDao userDao;
  private final RoleDao roleDao;

  public Optional<User> validateNewUserByName(String login) {
    Optional<User> existUserOptional = userDao.findByLogin(login);
    if (existUserOptional.isPresent()) {
      User existUser = existUserOptional.get();
      log.warn(
          "USER-VALIDATOR-001: Current User with name "
              + existUser.getFirstName()
              + " and email "
              + existUser.getEmail()
              + " is exist");
    }

    return existUserOptional;
  }

  public Optional<Role> validateNewUserRole(String role) {
    Optional<Role> optionalRole = roleDao.findByRole(role);
    if (optionalRole.isEmpty()) {
      log.error("USER-VALIDATOR-002: Role is not correct!");
    }

    return optionalRole;
  }
}
