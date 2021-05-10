package dplatonov.portal.validate;

import dplatonov.portal.dao.CompanyDao;
import dplatonov.portal.entity.Company;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompanyValidator {
  private static final Logger log = LoggerFactory.getLogger(UserValidator.class);
  private final CompanyDao dao;

  public Optional<Company> isExist(Long id) {
    Optional<Company> existCompanyOptional = dao.findById(id);
    if (existCompanyOptional.isEmpty()) {
      log.warn("COMPANY-VALIDATOR-001: Company with id " + id + " is not exist");
    }

    return existCompanyOptional;
  }

  public Optional<Company> isExist(String name) {
    Optional<Company> existCompanyOptional = dao.findByName(name);
    if (existCompanyOptional.isPresent()) {
      log.warn("COMPANY-VALIDATOR-001: Company with name " + name + " is exist");
    }

    return existCompanyOptional;
  }
}
