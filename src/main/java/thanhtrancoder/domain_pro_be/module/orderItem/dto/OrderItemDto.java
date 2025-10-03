package thanhtrancoder.domain_pro_be.module.orderItem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long orderItemId;
    private Long orderId;
    private String domainName;
    private String domainExtend;
    private Integer period;
    private Long price;
}
