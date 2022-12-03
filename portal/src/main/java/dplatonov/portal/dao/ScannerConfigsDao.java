package dplatonov.portal.dao;

import dplatonov.portal.entity.ScannerConfigs;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScannerConfigsDao extends JpaRepository<ScannerConfigs, Long> {

  @Query("Select s From ScannerConfigs s where s.user.id = :userId and s.id = :id and s.active is true")
  Optional<ScannerConfigs> findByUserIdAndId(@Param("userId") Long userId, @Param("id") Long id);

  @Query("Select s From ScannerConfigs s where s.user.id = :id and s.active is true")
  Optional<List<ScannerConfigs>> findAllByUserId(@Param("id") Long id);

  @Query("Select s from ScannerConfigs s where s.active is true")
  List<ScannerConfigs> findAllAndActive();
}
