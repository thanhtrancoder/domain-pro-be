package thanhtrancoder.domain_pro_be.module.orderItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    Boolean existsByOrderIdAndDomainNameAndIsDeleted(
            Long orderId,
            String domainName,
            Boolean isDeleted
    );

    Page<OrderItemEntity> findAllByOrderIdAndIsDeleted(
            Long orderId,
            Boolean isDeleted,
            Pageable pageable
    );
}
