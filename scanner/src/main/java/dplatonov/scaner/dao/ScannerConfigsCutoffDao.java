package dplatonov.scaner.dao;

import dplatonov.scaner.entity.ScannerConfigCutoff;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScannerConfigsCutoffDao extends JpaRepository<ScannerConfigCutoff, Long> {

  @Query("Select scc from ScannerConfigCutoff scc where scc.scannerId = :id")
  Optional<ScannerConfigCutoff> findByScannerId(Long id);

}
