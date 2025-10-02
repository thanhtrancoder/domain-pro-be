package thanhtrancoder.domain_pro_be.module.cart;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thanhtrancoder.domain_pro_be.module.base.BaseEntity;

@Entity
@Table(name = "cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id", nullable = false)
    private Long cartId;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "domain_name", nullable = false, length = 255)
    private String domainName;

    @Column(name = "domain_extend_id", nullable = false)
    private Long domainExtendId;

    @Column(name = "period", nullable = false)
    private Integer period;
}
