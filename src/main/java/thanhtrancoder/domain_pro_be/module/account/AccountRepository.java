package thanhtrancoder.domain_pro_be.module.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    AccountEntity findByEmail(String email);
    
    Boolean existsByEmail(String email);

    Boolean existsByEmailAndIsDeleted(String email, Boolean isDeleted);

    AccountEntity findOneByEmailAndIsDeleted(String email, Boolean isDeleted);

    Boolean existsByGoogleId(String googleId);

    AccountEntity findOneByAccountIdAndIsDeleted(Long accountId, Boolean isDeleted);

    AccountEntity findOneByGoogleIdAndIsDeleted(String googleId, Boolean isDeleted);
}
