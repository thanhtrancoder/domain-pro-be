package thanhtrancoder.domain_pro_be.module.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;
import thanhtrancoder.domain_pro_be.module.accountRole.AccountRoleService;

import java.time.LocalDateTime;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountRoleService accountRoleService;

    public AccountEntity getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public AccountEntity getActiveAccountByEmail(String email) {
        return accountRepository.findOneByEmailAndIsDeleted(email, false);
    }

    @Transactional
    public AccountEntity create(AccountEntity account) {
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new CustomException("Email này đã tồn tại, vui lòng chọn email khác");
        }

        try {
            account.setCreatedAt(LocalDateTime.now());
            account.setCreatedBy(0L);
            account.setIsDeleted(false);

            AccountEntity accountNew = accountRepository.save(account);
            accountRoleService.insert(accountNew.getAccountId(), 2L, accountNew.getAccountId());

            return accountNew;
        } catch (Exception e) {
            throw new QueryException("Có lỗi xảy ra khi tạo tài khoản.", e);
        }
    }

    public AccountEntity loginByEmail(String email) {
        AccountEntity account = accountRepository.findOneByEmailAndIsDeleted(
                email,
                false);
        if (account == null) {
            throw new CustomException("Sai tên tài khoản hoặc mật khẩu");
        }
        return account;
    }

    public Boolean existsByGoogleId(String googleId) {
        return accountRepository.existsByGoogleId(googleId);
    }
}
