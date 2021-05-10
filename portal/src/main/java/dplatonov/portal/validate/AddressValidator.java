package dplatonov.portal.validate;

import dplatonov.portal.dao.AddressDao;
import dplatonov.portal.entity.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AddressValidator {
  private final AddressDao dao;

  public Optional<Address> isNotExist(Address address) {
    return dao.findAddressByAddress(address.getAddress());
  }
}
