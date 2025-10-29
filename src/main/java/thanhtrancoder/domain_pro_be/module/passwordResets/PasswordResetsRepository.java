package thanhtrancoder.domain_pro_be.module.passwordResets;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PasswordResetsRepository extends JpaRepository<PasswordResetsEntity, Long> {
    // @formatter:off
    @Modifying
    @Query(nativeQuery = true, value = "" +
            "UPDATE password_resets " +
            "SET " +
                "is_deleted = 1, " +
                "updated_at = NOW(), " +
                "updated_by = 0 " +
            "WHERE " +
                "is_deleted = 0 " +
                "AND account_id = :accountId " +
                "AND used = 0"
    )
    void cancelAllOldOtp(Long accountId);
    // @formatter:on

    PasswordResetsEntity findOneByAccountIdAndUsedAndIsDeleted(Long accountId, Boolean used, Boolean isDeleted);
}
