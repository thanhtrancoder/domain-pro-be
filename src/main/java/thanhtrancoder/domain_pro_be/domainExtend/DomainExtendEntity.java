package thanhtrancoder.domain_pro_be.domainExtend;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thanhtrancoder.domain_pro_be.base.BaseEntity;

@Entity
@Table(
        name = "domain_extend",
        uniqueConstraints = {
                @UniqueConstraint(name = "name_UNIQUE", columnNames = "name")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomainExtendEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "domain_extend_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "base_price", nullable = false)
    private Integer basePrice;
}
