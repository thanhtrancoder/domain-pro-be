package thanhtrancoder.domain_pro_be.module.discountPrice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountPriceDto {
    private Long discountPriceId;
    private Long domainExtend;
    private Integer numberOfYear;
    private BigDecimal discount;
}
