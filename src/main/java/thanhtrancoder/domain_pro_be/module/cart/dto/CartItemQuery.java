package thanhtrancoder.domain_pro_be.module.cart.dto;

import java.math.BigDecimal;

public interface CartItemQuery {
    Long getCartId();

    Long getAccountId();

    String getDomainName();

    Long getDomainExtendId();

    Integer getPeriod();

    String getDomainExtend();

    BigDecimal getBasePrice();

    BigDecimal getDiscountPrice();

    Integer getIsAvailable();
}
