package dplatonov.scaner.dao;

import dplatonov.scaner.entity.ScannerResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScannerResultDao extends JpaRepository<ScannerResult, Long> {

}
