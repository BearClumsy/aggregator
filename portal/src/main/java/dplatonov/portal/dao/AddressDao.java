package dplatonov.portal.dao;

import dplatonov.portal.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressDao extends JpaRepository<Address, Long> {
  Optional<Address> findAddressByAddress(String address);
}
