package dplatonov.portal.service;

import dplatonov.portal.dao.CompanyDao;
import dplatonov.portal.entity.Address;
import dplatonov.portal.entity.Company;
import dplatonov.portal.payload.AddressPayload;
import dplatonov.portal.payload.CompanyPayload;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
  private final CompanyDao dao;

  public List<CompanyPayload> getCompanies() {
    return dao.findAll().stream()
        .map(CompanyService::mapCompanyToPayload)
        .collect(Collectors.toList());
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
        .build();
  }
}
