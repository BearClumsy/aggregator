package dplatonov.scaner.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company", schema = "aggregator")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
  @Id
  @Column(name = "id", nullable = false, unique = true)
  @SequenceGenerator(
      name = "company_id_seq",
      sequenceName = "company_id_seq",
      schema = "aggregator",
      allocationSize = 1)
  @GeneratedValue(generator = "company_id_seq", strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "city")
  private String city;

  @Column(name = "description")
  private String description;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "company_id")
  private List<Address> addresses;
}
