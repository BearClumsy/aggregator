package dplatonov.scaner.validator;

import dplatonov.scaner.dao.CompanyDao;
import dplatonov.scaner.entity.Company;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompanyValidator {
  private static final Logger log = LoggerFactory.getLogger(CompanyValidator.class);
  private final CompanyDao dao;

  public Optional<Company> isExist(String name) {
    Optional<Company> existCompanyOptional = dao.findByName(name);
    if (existCompanyOptional.isPresent()) {
      log.warn("COMPANY-VALIDATOR-001: Company with name " + name + " is exist");
    }

    return existCompanyOptional;
  }
}
