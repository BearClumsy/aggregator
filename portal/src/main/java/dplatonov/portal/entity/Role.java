package dplatonov.portal.entity;

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
@Table(name = "roles", schema = "aggregator")
@Data
@NoArgsConstructor
public class Role {
  @Id
  @Column(name = "id", nullable = false, unique = true)
  @SequenceGenerator(
      name = "roles_id_seq",
      sequenceName = "roles_id_seq",
      schema = "aggregator",
      allocationSize = 1)
  @GeneratedValue(generator = "roles_id_seq", strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "roles", nullable = false)
  private String role;
}
