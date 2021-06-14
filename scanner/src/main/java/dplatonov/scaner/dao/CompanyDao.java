package dplatonov.scaner.dao;

import dplatonov.scaner.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyDao extends JpaRepository<Company, Long> {
  Optional<Company> findByName(String name);
}
