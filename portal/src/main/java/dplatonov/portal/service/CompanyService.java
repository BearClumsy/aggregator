package dplatonov.portal.service;

import dplatonov.portal.dao.CompanyDao;
import dplatonov.portal.entity.Address;
import dplatonov.portal.entity.Company;
import dplatonov.portal.payload.AddressPayload;
import dplatonov.portal.payload.CompanyPayload;
import dplatonov.portal.validate.CompanyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
  private final CompanyDao dao;
  private final CompanyValidator validator;
  private final UserService userService;

  public List<CompanyPayload> getCompanies() {
    boolean isAdmin = userService.isAdmin();
    List<CompanyPayload> companies;
    if (isAdmin) {
      companies =
          dao.findAll().stream()
              .map(CompanyService::mapCompanyToPayload)
              .collect(Collectors.toList());
    } else {
      companies =
          dao.findAllByActive(true).stream()
              .map(CompanyService::mapCompanyToPayload)
              .collect(Collectors.toList());
    }
    return companies;
  }

  public CompanyPayload create(CompanyPayload newCompany) {
    Company company = mapCompanyPayloadToCompany(newCompany);
    Optional<Company> existCompanyOptional = validator.isExist(company.getName());
    if (existCompanyOptional.isPresent()) {
      return null;
    }

    Company saved = dao.save(company);
    return mapCompanyToPayload(saved);
  }

  public CompanyPayload update(CompanyPayload changes) {
    Company company = mapCompanyPayloadToCompany(changes);
    Optional<Company> existCompanyOptional = validator.isExist(company.getId());
    if (existCompanyOptional.isEmpty()) {
      return null;
    }

    Company saved = dao.save(company);
    return mapCompanyToPayload(saved);
  }

  public CompanyPayload markAsDelete(CompanyPayload changes) {
    Company company = mapCompanyPayloadToCompany(changes);
    Optional<Company> existCompanyOptional = validator.isExist(company.getId());
    if (existCompanyOptional.isEmpty()) {
      return null;
    }
    company.setActive(false);
    Company saved = dao.save(company);
    return mapCompanyToPayload(saved);
  }

  private Address mapAddressPayloadToAddress(AddressPayload addressPayload) {
    return Address.builder()
        .id(addressPayload.getId())
        .city(addressPayload.getCity())
        .address(addressPayload.getAddress())
        .build();
  }

  private Company mapCompanyPayloadToCompany(CompanyPayload payload) {
    List<Address> addresses =
        payload.getAddresses().stream()
            .map(this::mapAddressPayloadToAddress)
            .collect(Collectors.toList());
    return Company.builder()
        .id(payload.getId())
        .city(payload.getCity())
        .description(payload.getDescription())
        .name(payload.getName())
        .addresses(addresses)
        .active(payload.isActive())
        .build();
  }

  private static CompanyPayload mapCompanyToPayload(Company company) {
    List<AddressPayload> addressPayload = getAddressPayload(company);
    return getCompanyPayload(company, addressPayload);
  }

  private static List<AddressPayload> getAddressPayload(Company company) {
    return company.getAddresses().stream()
        .map(CompanyService::mapAddressToPayload)
        .collect(Collectors.toList());
  }

  private static AddressPayload mapAddressToPayload(Address address) {
    return AddressPayload.builder()
        .id(address.getId())
        .city(address.getCity())
        .address(address.getAddress())
        .build();
  }

  private static CompanyPayload getCompanyPayload(
      Company company, List<AddressPayload> addressPayloads) {
    return CompanyPayload.builder()
        .id(company.getId())
        .name(company.getName())
        .city(company.getCity())
        .description(company.getDescription())
        .addresses(addressPayloads)
        .active(company.isActive())
        .build();
  }
}
