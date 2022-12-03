package dplatonov.portal.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.Hibernate;

@Entity
@Table(name = "users", schema = "aggregator")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  @Id
  @Column(name = "id")
  @SequenceGenerator(
      name = "users_id_seq",
      sequenceName = "users_id_seq",
      schema = "aggregator",
      allocationSize = 1)
  @GeneratedValue(generator = "users_id_seq", strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "second_name", nullable = false)
  private String secondName;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "login", nullable = false)
  private String login;

  @Column(name = "active")
  private boolean active;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "roles_id")
  @Exclude
  private Role role;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    User user = (User) o;
    return id != null && Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
