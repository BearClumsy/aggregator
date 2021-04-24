package dplatonov.portal.controller;

import dplatonov.portal.payload.CompanyPayload;
import dplatonov.portal.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {
  private final CompanyService service;

  @GetMapping
  public ResponseEntity<List<CompanyPayload>> getCompanies() {
    List<CompanyPayload> companies = service.getCompanies();
    return ResponseEntity.status(HttpStatus.OK).body(companies);
  }
}
