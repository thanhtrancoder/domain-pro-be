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

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "gateway", nullable = false, length = 45)
    private String gateway;

    @Column(name = "transaction_id", nullable = false, length = 45)
    private String transactionId;

    @Column(name = "amount", nullable = false)
    private Long amount;
}
