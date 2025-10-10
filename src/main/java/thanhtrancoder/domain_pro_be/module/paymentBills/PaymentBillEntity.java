package thanhtrancoder.domain_pro_be.module.paymentBills;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thanhtrancoder.domain_pro_be.module.base.BaseEntity;

@Entity
@Table(name = "payment_bills")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentBillEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_bills_id", nullable = false)
    private Long paymentBillsId;

    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @Column(name = "partner_code", nullable = false)
    private String partnerCode;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "request_id", nullable = false)
    private String requestId;

    @Column(name = "amount", nullable = false)
    private String amount;

    @Column(name = "order_info")
    private String orderInfo;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "trans_id", nullable = false)
    private String transId;

    @Column(name = "result_code", nullable = false)
    private String resultCode;

    @Column(name = "message")
    private String message;

    @Column(name = "pay_type")
    private String payType;

    @Column(name = "response_time")
    private String responseTime;

    @Column(name = "extra_data")
    private String extraData;
}
