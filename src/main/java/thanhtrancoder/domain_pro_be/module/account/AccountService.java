package thanhtrancoder.domain_pro_be.module.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;
import thanhtrancoder.domain_pro_be.module.account.dto.AccountProfileRes;
import thanhtrancoder.domain_pro_be.module.account.dto.AccountUpdateReq;
import thanhtrancoder.domain_pro_be.module.accountRole.AccountRoleService;
import thanhtrancoder.domain_pro_be.module.cart.CartService;
import thanhtrancoder.domain_pro_be.module.cart.dto.CartDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountRoleService accountRoleService;
    @Autowired
    private CartService cartService;

    public AccountEntity getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public AccountEntity getActiveAccountByEmail(String email) {
        return accountRepository.findOneByEmailAndIsDeleted(email, false);
    }

    @Transactional
    public AccountEntity create(AccountEntity account) {
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new CustomException("Email này đã tồn tại, vui lòng sử dụng email khác");
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

    public AccountProfileRes getProfile(Long accountId) {
        AccountEntity account = accountRepository.findOneByAccountIdAndIsDeleted(
                accountId,
                false
        );
        if (account == null) {
            throw new CustomException("Tài khoản không tồn tại");
        }

        List<String> roles = accountRoleService.getRolesByAccountId(accountId);

        Page<CartDto> cartList = cartService.getAll(accountId, Pageable.unpaged());

        AccountProfileRes accountProfileRes = new AccountProfileRes();
        accountProfileRes.setFullname(account.getFullname());
        accountProfileRes.setEmail(account.getEmail());
        accountProfileRes.setIsVerify(account.getIsVerify());
        accountProfileRes.setRoles(roles);
        accountProfileRes.setNumberCartItem((long) cartList.getContent().size());
        accountProfileRes.setCreatedAt(account.getCreatedAt());

        return accountProfileRes;
    }

    public AccountEntity getAccountByGoogleId(String googleId) {
        return accountRepository.findOneByGoogleIdAndIsDeleted(googleId, false);
    }

    public AccountProfileRes getProfileByAccount(AccountEntity account) {

        List<String> roles = accountRoleService.getRolesByAccountId(account.getAccountId());

        Page<CartDto> cartList = cartService.getAll(account.getAccountId(), Pageable.unpaged());

        AccountProfileRes accountProfileRes = new AccountProfileRes();
        accountProfileRes.setFullname(account.getFullname());
        accountProfileRes.setEmail(account.getEmail());
        accountProfileRes.setIsVerify(account.getIsVerify());
        accountProfileRes.setRoles(roles);
        accountProfileRes.setNumberCartItem((long) cartList.getContent().size());
        accountProfileRes.setCreatedAt(account.getCreatedAt());

        return accountProfileRes;
    }

    @Transactional
    public AccountEntity update(AccountUpdateReq accountUpdateReq, Long accountId) {
        Optional<AccountEntity> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) {
            throw new CustomException("Tài khoản không tồn tại");
        }

        AccountEntity account = accountOptional.get();

        try {

            if (accountUpdateReq.getFullname() != null) {
                account.setFullname(accountUpdateReq.getFullname());
            }
            if (accountUpdateReq.getPasswordEncoded() != null) {
                account.setPassword(accountUpdateReq.getPasswordEncoded());
            }
            account.setUpdatedBy(accountId);
            account.setUpdatedAt(LocalDateTime.now());
            return accountRepository.save(account);
        } catch (Exception e) {
            throw new QueryException("Có lỗi xảy ra khi cập nhật tài khoản.", e);
        }

    }
}
