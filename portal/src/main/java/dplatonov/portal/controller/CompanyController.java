package dplatonov.portal.controller;

import dplatonov.portal.payload.CompanyPayload;
import dplatonov.portal.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {
  private final CompanyService service;

  @GetMapping
  public ResponseEntity<List<CompanyPayload>> get() {
    return ResponseEntity.status(HttpStatus.OK).body(service.getCompanies());
  }

  @PostMapping
  public ResponseEntity<CompanyPayload> create(@RequestBody CompanyPayload newCompany) {
    CompanyPayload companyPayload = service.create(newCompany);
    if (Objects.isNull(companyPayload)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    return ResponseEntity.status(HttpStatus.OK).body(companyPayload);
  }

  @PutMapping
  public ResponseEntity<CompanyPayload> update(@RequestBody CompanyPayload changes) {
    CompanyPayload companyPayload = service.update(changes);
    if (Objects.isNull(companyPayload)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    return ResponseEntity.status(HttpStatus.OK).body(companyPayload);
  }
}
