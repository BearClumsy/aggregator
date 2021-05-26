package dplatonov.scaner.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "config", schema = "aggregator")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Config {
  @Id
  @GeneratedValue(generator = "config_id_seq", strategy = GenerationType.AUTO)
  @SequenceGenerator(
      name = "config_id_seq",
      sequenceName = "config_id_seq",
      schema = "aggregator",
      allocationSize = 1)
  private Long id;

  @Column(name = "scanner_page_num", nullable = false)
  private Integer scannerPageNum;
}
