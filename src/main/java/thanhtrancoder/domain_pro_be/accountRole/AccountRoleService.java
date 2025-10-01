package thanhtrancoder.domain_pro_be.accountRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountRoleService {
    @Autowired
    private AccountRoleRepository accountRoleRepository;

    public List<String> getRolesByAccountId(Long accountId) {
        return accountRoleRepository.findByAccountId(accountId);
    }
}
