package dplatonov.portal.controller;

import dplatonov.portal.payload.UserPayload;
import dplatonov.portal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
  private final UserService service;

  @GetMapping
  public ResponseEntity<List<UserPayload>> getUsers() {
    return ResponseEntity.status(HttpStatus.OK).body(service.getUsers());
  }
}
