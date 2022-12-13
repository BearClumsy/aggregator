package dplatonov.portal.dao;

import dplatonov.portal.entity.ScannerPreview;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScannerPreviewDao extends JpaRepository<ScannerPreview, Long> {

  @Query("Select pr from ScannerPreview pr where pr.scannerId = :id")
  List<ScannerPreview> findByScannerId(Long id);
}
