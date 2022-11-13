package dplatonov.portal.dao;

import dplatonov.portal.entity.ScannerConfigs;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScannerConfigsDao extends JpaRepository<ScannerConfigs, Long> {
  List<ScannerConfigs> findAllByActive(Boolean active);
  Optional<ScannerConfigs> findById(Long id);

}
