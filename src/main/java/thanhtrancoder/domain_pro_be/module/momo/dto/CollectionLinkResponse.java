package thanhtrancoder.domain_pro_be.module.momo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionLinkResponse {
    private String partnerCode;
    private String requestId;
    private String orderId;
    private long amount;
    private long responseTime;
    private String message;
    private int resultCode;
    private String payUrl;
    private String shortLink;
}
