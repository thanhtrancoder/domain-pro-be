package thanhtrancoder.domain_pro_be.module.momo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckPaymentRes {
    private String orderId;
    private Long amount;
    private LocalDateTime createdAt;
}
