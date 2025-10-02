package thanhtrancoder.domain_pro_be.module.accountRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRoleRepository extends JpaRepository<AccountRoleEntity, Long> {
    @Query(nativeQuery = true,
            value = "SELECT r.name " +
                    "FROM account_role ar JOIN role r ON ar.role_id = r.role_id " +
                    "WHERE account_id = :accountId")
    List<String> findByAccountId(Long accountId);
}
