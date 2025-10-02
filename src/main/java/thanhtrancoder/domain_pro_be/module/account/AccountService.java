package thanhtrancoder.domain_pro_be.module.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;

import java.time.LocalDateTime;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public AccountEntity getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public AccountEntity create(AccountEntity account) {
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new CustomException("Email này đã tồn tại, vui lòng chọn email khác");
        }

        account.setCreatedAt(LocalDateTime.now());
        account.setCreatedBy(0L);
        account.setIsDeleted(false);
        return accountRepository.save(account);
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
