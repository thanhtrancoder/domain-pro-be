package thanhtrancoder.domain_pro_be.module.discountPrice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thanhtrancoder.domain_pro_be.module.base.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "discount_price")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountPriceEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_price_id", nullable = false)
    private Long discountPriceId;

    @Column(name = "domain_extend_id", nullable = false)
    private Long domainExtend;

    @Column(name = "number_of_year", nullable = false)
    private Integer numberOfYear;

    @Column(name = "discount", nullable = false, precision = 5, scale = 2)
    private BigDecimal discount;
}
