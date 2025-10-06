package thanhtrancoder.domain_pro_be.module.cart.dto;

import java.math.BigDecimal;

public interface CartPriceQuery {
    String getDomainExtend();

    BigDecimal getPrice();

    BigDecimal getDiscountPrice();
}
