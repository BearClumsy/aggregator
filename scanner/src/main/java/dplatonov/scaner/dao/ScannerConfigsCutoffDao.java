package dplatonov.scaner.dao;

import dplatonov.scaner.entity.ScannerConfigCutoff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScannerConfigsCutoffDao extends JpaRepository<ScannerConfigCutoff, Long> {

}
