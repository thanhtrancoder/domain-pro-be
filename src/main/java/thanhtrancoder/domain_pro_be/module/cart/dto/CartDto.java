package thanhtrancoder.domain_pro_be.module.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long cartId;
    private Long accountId;
    private String domainName;
    private Long domainExtendId;
    private Integer period;
}
