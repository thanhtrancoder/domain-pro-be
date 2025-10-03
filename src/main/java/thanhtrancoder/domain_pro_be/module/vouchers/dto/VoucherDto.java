package thanhtrancoder.domain_pro_be.module.vouchers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherDto {
    private Long vouchersId;
    private String code;
    private String discountType; // mapped from enum in entity
    private Long discountValue;
    private Long minOrderValue;
    private Long maxDiscountAmount;
    private Integer usageLimit;
    private Integer usagePerUser;
    private LocalDateTime startAt;
    private LocalDateTime expiresAt;
}
