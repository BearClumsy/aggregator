package dplatonov.scaner.dao;

import dplatonov.scaner.entity.ScannerResult;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScannerResultDao extends JpaRepository<ScannerResult, Long> {

  @Query("Select sr from ScannerResult sr where sr.scannerId = :id")
  List<ScannerResult> findAllByScannerId(Long id);

}
