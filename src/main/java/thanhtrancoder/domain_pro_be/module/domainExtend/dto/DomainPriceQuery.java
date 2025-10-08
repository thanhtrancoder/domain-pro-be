package thanhtrancoder.domain_pro_be.module.domainExtend.dto;

import java.math.BigDecimal;

public interface DomainPriceQuery {
    String getDomainExtend();

    BigDecimal getPrice();

    BigDecimal getDiscountPrice();
}
