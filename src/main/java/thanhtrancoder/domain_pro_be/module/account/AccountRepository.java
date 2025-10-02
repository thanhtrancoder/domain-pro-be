package thanhtrancoder.domain_pro_be.module.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    public AccountEntity findByEmail(String email);
    public Boolean existsByEmail(String email);
    public AccountEntity findOneByEmailAndIsDeleted(String email, Boolean isDeleted);
    public Boolean existsByGoogleId(String googleId);
}
