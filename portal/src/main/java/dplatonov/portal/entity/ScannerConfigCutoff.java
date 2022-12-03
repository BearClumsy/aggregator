package dplatonov.portal.entity;

import java.util.Date;
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
@Table(name = "scanner_configs_cutoff", schema = "aggregator")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ScannerConfigCutoff {
  @Id
  @Column(name = "id", nullable = false, unique = true)
  @GeneratedValue(generator = "scanner_configs_cutoff_id_seq", strategy = GenerationType.AUTO)
  @SequenceGenerator(name = "scanner_configs_cutoff_id_seq", schema = "aggregator", sequenceName = "scanner_configs_cutoff_id_seq", allocationSize = 1)
  private Long id;

  @Column(name = "start_date")
  private Date startDate;

  @Column(name = "stop_date")
  private Date stopDte;

  @Column(name = "is_interrupted")
  private Boolean isInterrupted;

}
