package dplatonov.portal.controller;

import dplatonov.portal.payload.UserPayload;
import dplatonov.portal.service.AuthService;
import dplatonov.portal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@CrossOrigin
@Validated
@RequestMapping(value = "/api")
public class AuthController {
  private static final Logger log = LoggerFactory.getLogger(AuthController.class);
  private final UserService userService;
  private final AuthService authService;

  @Autowired
  public AuthController(UserService userService, AuthService authService) {
    this.userService = userService;
    this.authService = authService;
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping(value = "/login")
  public ResponseEntity<UserPayload> login() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();
    log.info("AUTHORIZATION-CONTROLLER-OO1: Login is successful..");
    return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByEmail(email));
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PutMapping("/registration")
  public ResponseEntity<UserPayload> registration(@Valid @RequestBody UserPayload user) {
    UserPayload newUser = authService.createUser(user);
    if (Objects.isNull(newUser)) {
      ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
    }

    log.info("AUTHORIZATION-CONTROLLER-OO2: Registration is successful..");
    return ResponseEntity.status(HttpStatus.OK).body(newUser);
  }
}
