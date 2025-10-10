package thanhtrancoder.domain_pro_be.module.paymentBills;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentBillRepository extends JpaRepository<PaymentBillEntity, Long> {
    Boolean existsByOrderIdAndIsDeleted(String orderId, Boolean isDeleted);

    PaymentBillEntity findOneByOrderIdAndIsDeleted(String orderId, Boolean isDeleted);
}
