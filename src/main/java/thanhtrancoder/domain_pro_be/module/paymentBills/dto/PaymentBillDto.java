package thanhtrancoder.domain_pro_be.module.paymentBills.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentBillDto {
    private Long paymentBillsId;
    private Long orderId;
    private String gateway;
    private String transactionId;
    private Long amount;
}
