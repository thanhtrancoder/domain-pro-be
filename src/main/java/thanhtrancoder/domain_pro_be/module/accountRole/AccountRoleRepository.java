package thanhtrancoder.domain_pro_be.module.accountRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRoleRepository extends JpaRepository<AccountRoleEntity, Long> {
    // @formatter:off
    @Query(nativeQuery = true,
            value = "SELECT r.name " +
                    "FROM account_role ar JOIN role r ON ar.role_id = r.role_id " +
                    "WHERE " +
                        "ar.is_deleted = 0 " +
                        "AND r.is_deleted = 0 " +
                        "AND ar.account_id = :accountId")
    List<String> findByAccountId(Long accountId);
    // @formatter:on
}
