package thanhtrancoder.domain_pro_be.module.paymentMethod.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodDto {
    private Long paymentMethodId;
    private String name;
}
