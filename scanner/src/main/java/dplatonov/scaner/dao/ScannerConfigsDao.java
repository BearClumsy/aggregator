package dplatonov.scaner.dao;


import dplatonov.scaner.entity.ScannerConfigs;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScannerConfigsDao extends JpaRepository<ScannerConfigs, Long> {

  @Query("select s from ScannerConfigs s JOIN FETCH s.scannerSteps st where s.id = :id and s.active is true order by st.id")
  Optional<ScannerConfigs> findByIdAndActive(Long id);

}
