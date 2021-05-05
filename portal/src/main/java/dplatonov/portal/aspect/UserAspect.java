package dplatonov.portal.aspect;

import dplatonov.portal.dao.UserDao;
import dplatonov.portal.entity.Role;
import dplatonov.portal.entity.User;
import dplatonov.portal.enums.AccessConstants;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class UserAspect {
  private static final Logger log = LogManager.getLogger(UserAspect.class);
  private final UserDao dao;

  @Pointcut("@annotation(dplatonov.portal.annatation.Admin)")
  public void adminAccessPointcutDefinition() {}

  @Pointcut("@annotation(dplatonov.portal.annatation.Participant)")
  public void participantAccessPointcutDefinition() {}

  @Before("adminAccessPointcutDefinition()")
  public void validateUsersIsAnAdminRole() {
    checkUserRole(AccessConstants.ADMIN);
  }

  @Before("participantAccessPointcutDefinition()")
  public void validateUsersIsAParticipantRole() {
    checkUserRole(AccessConstants.PARTICIPANT);
  }

  private void checkUserRole(AccessConstants type) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();
    User user = dao.findByEmail(email);
    Role role = user.getRole();
    if (!Objects.equals(role.getRole(), type.toString())) {
      log.error("USER-ASPECT-001: User does not have access to the requested resource.");
      throw new AccessDeniedException("You don't have access to the requested resource.");
    }
  }
}
