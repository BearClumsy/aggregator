package dplatonov.portal.controller;

import dplatonov.portal.annatation.Admin;
import dplatonov.portal.payload.UserPayload;
import dplatonov.portal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
  private final UserService service;

  @Admin
  @GetMapping
  public ResponseEntity<List<UserPayload>> getUsers() {
    return ResponseEntity.status(HttpStatus.OK).body(service.getUsers());
  }

  @Admin
  @PostMapping
  public ResponseEntity<UserPayload> createUser(@RequestBody UserPayload newUser) {
    return ResponseEntity.status(HttpStatus.OK).body(service.createUser(newUser));
  }

  @Admin
  @PutMapping
  public ResponseEntity<UserPayload> updateUser(@RequestBody UserPayload user){
    return ResponseEntity.status(HttpStatus.OK).body(service.updateUser(user));
  }

  @Admin
  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable("id") Long id) {
    service.deleteUser(id);
  }
}
