package dplatonov.scaner.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "scanner_result", schema = "aggregator")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ScannerResult {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "scanner_result_seq")
  @SequenceGenerator(name = "scanner_result_seq", sequenceName = "scanner_result_seq", schema = "aggregator", allocationSize = 1)
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @Column(name = "scanner_id", nullable = false)
  private Long scannerId;

  @Column(name = "value", nullable = false)
  private String value;
}
