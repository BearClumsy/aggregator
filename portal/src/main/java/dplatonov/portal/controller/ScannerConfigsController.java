package dplatonov.portal.controller;

import dplatonov.portal.annatation.Admin;
import dplatonov.portal.payload.ScannerConfigsPayload;
import dplatonov.portal.service.ScannerConfigsService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scanner-configs")
@RequiredArgsConstructor
public class ScannerConfigsController {
  private final ScannerConfigsService service;

  @Admin
  @GetMapping
  public ResponseEntity<List<ScannerConfigsPayload>> get() {
    return ResponseEntity.status(HttpStatus.OK).body(service.get());
  }

  @Admin
  @PostMapping
  public ResponseEntity<ScannerConfigsPayload> create(@RequestBody ScannerConfigsPayload payload){
    ScannerConfigsPayload result = service.create(payload);
    if (Objects.isNull(result)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @Admin
  @PutMapping
  public ResponseEntity<ScannerConfigsPayload> update(@RequestBody ScannerConfigsPayload payload){
    ScannerConfigsPayload result = service.update(payload);
    if (Objects.isNull(result)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @Admin
  @PatchMapping
  public ResponseEntity<ScannerConfigsPayload> markAsDelete(@RequestBody ScannerConfigsPayload payload){
    ScannerConfigsPayload result = service.markAsDelete(payload);
    if (Objects.isNull(result)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

}
