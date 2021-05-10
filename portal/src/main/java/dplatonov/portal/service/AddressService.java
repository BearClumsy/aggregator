package dplatonov.portal.service;

import dplatonov.portal.dao.AddressDao;
import dplatonov.portal.entity.Address;
import dplatonov.portal.validate.AddressValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {
  private final AddressDao dao;
  private final AddressValidator validator;

  public void create(List<Address> addresses) {
    addresses.stream().map(validator::isNotExist).filter(Optional::isEmpty).map(Optional::get).forEach(dao::save);
  }
}
