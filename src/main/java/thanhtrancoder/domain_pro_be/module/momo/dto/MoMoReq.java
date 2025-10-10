package thanhtrancoder.domain_pro_be.module.momo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoMoReq {
    private String partnerCode;
    private String orderId;
    private String requestId;
    private String amount;
    private String orderInfo;
    private String orderType;
    private String transId;
    private String resultCode;
    private String message;
    private String payType;
    private String responseTime;
    private String extraData;
    private String signature;
}
