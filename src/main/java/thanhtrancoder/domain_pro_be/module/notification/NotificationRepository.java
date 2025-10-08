package thanhtrancoder.domain_pro_be.module.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    Page<NotificationEntity> findAllByAccountIdAndIsDeleted(Long accountId, boolean isDeleted, Pageable pageable);
}
