package thanhtrancoder.domain_pro_be.module.orders;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    OrderEntity findOneByOrderIdAndIsDeleted(Long orderId, Boolean isDeleted);

    OrderEntity findOneByOrderIdAndAccountIdAndIsDeleted(
            Long orderId,
            Long accountId,
            Boolean isDeleted
    );
}
