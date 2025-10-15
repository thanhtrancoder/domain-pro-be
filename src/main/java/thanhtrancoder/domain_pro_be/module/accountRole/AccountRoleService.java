package thanhtrancoder.domain_pro_be.module.accountRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountRoleService {
    @Autowired
    private AccountRoleRepository accountRoleRepository;

//    public List<String> getRolesByAccountId(Long accountId) {
//        return accountRoleRepository.findByAccountId(accountId);
//    }

    @Transactional
    public AccountRoleEntity insert(Long accountId, Long roleId, Long createdBy) {
        try {
            AccountRoleEntity accountRole = new AccountRoleEntity();
            accountRole.setAccountId(accountId);
            accountRole.setRoleId(roleId);
            accountRole.setCreatedAt(LocalDateTime.now());
            accountRole.setCreatedBy(createdBy);
            accountRole.setIsDeleted(false);
            return accountRoleRepository.save(accountRole);
        } catch (Exception e) {
            throw new QueryException("có lỗi xảy ra khi thực hiện phân quyền cho tài khoản.", e);
        }
    }

    public List<String> getRolesByAccountId(Long accountId) {
        return accountRoleRepository.findByAccountId(accountId);
    }
}
