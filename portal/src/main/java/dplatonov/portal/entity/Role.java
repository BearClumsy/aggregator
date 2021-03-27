package dplatonov.portal.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "role", schema = "aggregator")
@Data
@NoArgsConstructor
public class Role {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @SequenceGenerator(name = "role_id_seq", sequenceName = "role_id_seq", schema = "aggregator", allocationSize = 1)
    @GeneratedValue(generator = "role_id_seq", strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "role")
    private String role;
}
