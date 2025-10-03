package thanhtrancoder.domain_pro_be.module.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long orderId;
    private Long accountId;
    private String fullname;
    private String email;
    private String phone;
    private String province;
    private String address;
    private Long paymentMethodId;
    private String discountCode;
    private Long discountPrice;
    private Long totalPrice;
    private Integer status;
}
