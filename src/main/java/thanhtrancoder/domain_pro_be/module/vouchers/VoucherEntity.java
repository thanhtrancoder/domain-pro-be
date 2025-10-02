package thanhtrancoder.domain_pro_be.module.vouchers;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thanhtrancoder.domain_pro_be.module.base.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vouchers_id", nullable = false)
    private Long vouchersId;

    @Column(name = "code", nullable = false, unique = true, length = 45)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 20)
    private DiscountType discountType;

    @Column(name = "discount_value", nullable = false)
    private Long discountValue;

    @Column(name = "min_order_value")
    private Long minOrderValue;

    @Column(name = "max_discount_amount")
    private Long maxDiscountAmount;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "usage_per_user")
    private Integer usagePerUser = 1;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    private enum DiscountType {
        fixed_amount,
        percentage
    }
}
