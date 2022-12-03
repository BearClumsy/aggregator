package dplatonov.scaner.entity;

import java.util.Objects;
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
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "scanner_steps", schema = "aggregator")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class ScannerSteps {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scanner_steps_seq")
  @SequenceGenerator(name = "scanner_steps_seq", sequenceName = "scanner_steps_seq", schema = "aggregator", allocationSize = 1)
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @Column(name = "tag")
  private String tag;

  @Column(name = "type")
  private String type;

  @Column(name = "action", nullable = false)
  private String action;

  @Column(name = "value")
  private String value;

  @Column(name = "active", nullable = false)
  private Boolean active;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    ScannerSteps that = (ScannerSteps) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
