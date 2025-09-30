package thanhtrancoder.domain_pro_be.domainExtend;

import jakarta.persistence.*;
import thanhtrancoder.domain_pro_be.base.BaseEntity;

@Entity
@Table(
        name = "domain_extend",
        schema = "domain_pro",
        uniqueConstraints = {
                @UniqueConstraint(name = "name_UNIQUE", columnNames = "name")
        }
)
public class DomainExtendEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "domain_extend_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "base_price", nullable = false)
    private Integer basePrice;
}
