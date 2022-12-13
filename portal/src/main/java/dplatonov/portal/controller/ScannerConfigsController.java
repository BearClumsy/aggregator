package dplatonov.portal.controller;

import dplatonov.portal.annatation.Participant;
import dplatonov.portal.payload.POJOResponsePayload;
import dplatonov.portal.payload.ScannerConfigsPayload;
import dplatonov.portal.payload.ScannerPreviewPayload;
import dplatonov.portal.service.ScannerConfigsService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scanner-configs")
@RequiredArgsConstructor
public class ScannerConfigsController {

  private final ScannerConfigsService service;

  @Participant
  @GetMapping
  public ResponseEntity<List<ScannerConfigsPayload>> get() {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(service.get());
    } catch (IllegalAccessException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }

  @Participant
  @PostMapping
  public ResponseEntity<ScannerConfigsPayload> create(@RequestBody ScannerConfigsPayload payload) {
    ScannerConfigsPayload result = service.create(payload);
    if (Objects.isNull(result)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @Participant
  @PutMapping
  public ResponseEntity<ScannerConfigsPayload> update(@RequestBody ScannerConfigsPayload payload) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(service.update(payload));
    } catch (IllegalAccessException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }

  @Participant
  @PatchMapping
  public ResponseEntity<ScannerConfigsPayload> markAsDelete(
      @RequestBody ScannerConfigsPayload payload) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(service.markAsDelete(payload));
    } catch (IllegalAccessException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }

  @Participant
  @PostMapping("/start")
  public ResponseEntity<ScannerConfigsPayload> start(@RequestBody ScannerConfigsPayload payload) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(service.start(payload));
    } catch (IllegalAccessException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }

  @Participant
  @PostMapping("/stop")
  public ResponseEntity<ScannerConfigsPayload> stop(@RequestBody ScannerConfigsPayload payload) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(service.stop(payload));
    } catch (IllegalAccessException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }

  @Participant
  @GetMapping("/check/{id}")
  public ResponseEntity<POJOResponsePayload> checkStatus(@PathVariable("id") Long id) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(service.checkStatus(id));
    } catch (IllegalAccessException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }

  @Participant
  @GetMapping("/preview/{id}")
  public ResponseEntity<List<ScannerPreviewPayload>> getPreview(@PathVariable("id") Long id) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(service.getPreview(id));
    } catch (IllegalAccessException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }

  @Participant
  @GetMapping(value = "/create/file/{type}/{id}")
  @ResponseBody
  public ResponseEntity<Resource> createFile(@PathVariable("type") String type,
      @PathVariable("id") Long id) {
    try {
      Resource resource = service.createFile(id, type);
      return ResponseEntity.status(HttpStatus.OK)
          .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + resource.getFilename() + "\"")
          .body(resource);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }

}
