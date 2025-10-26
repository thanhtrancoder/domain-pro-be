package thanhtrancoder.domain_pro_be.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;
import thanhtrancoder.domain_pro_be.common.utils.RegexUtils;
import thanhtrancoder.domain_pro_be.module.account.AccountEntity;
import thanhtrancoder.domain_pro_be.module.account.AccountService;
import thanhtrancoder.domain_pro_be.module.account.dto.AccountProfileRes;
import thanhtrancoder.domain_pro_be.module.account.dto.AccountUpdateReq;
import thanhtrancoder.domain_pro_be.module.cart.dto.CartDto;
import thanhtrancoder.domain_pro_be.security.auth.dto.LoginReq;
import thanhtrancoder.domain_pro_be.security.auth.dto.RegisterReq;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.security.auth.dto.UpdateReq;

import java.util.List;

@Service
public class AuthService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AccountEntity login(LoginReq loginReq) {
        AccountEntity account = accountService.loginByEmail(loginReq.getEmail());
        if (account == null) {
            throw new CustomException("Sai tên tài khoản hoặc mật khẩu");
        }
        if (!passwordEncoder.matches(loginReq.getPassword(), account.getPassword())) {
            throw new CustomException("Sai tên tài khoản hoặc mật khẩu");
        }
        if (account.getPassword() == null) {
            throw new CustomException("Sai tên tài khoản hoặc mật khẩu");
        }
        return account;
    }

    public void register(RegisterReq registerReq) {
        if (registerReq.getEmail() == null
                || registerReq.getEmail().trim().isEmpty()
                || !RegexUtils.EMAIL_REGEX.matcher(registerReq.getEmail().trim()).matches()) {
            throw new CustomException("Email không hợp lệ.");
        }
        if (!registerReq.getPassword().equals(registerReq.getConfirmPassword())) {
            throw new CustomException("Mật khẩu không khớp");
        }
        if (registerReq.getPassword().length() < 8) {
            throw new CustomException("Mật khẩu phải có ít nhất 8 ký tự");
        }
        if (registerReq.getPassword().toLowerCase().equals(registerReq.getPassword())) {
            throw new CustomException("Mật khẩu phải chứa chữ hoa");
        }
        if (registerReq.getPassword().toUpperCase().equals(registerReq.getPassword())) {
            throw new CustomException("Mật khẩu phải chứa chữ thường");
        }
        if (!registerReq.getPassword().matches(".*[0-9].*")) {
            throw new CustomException("Mật khẩu phải chứa số");
        }
        if (!registerReq.getPassword().matches(".*[!@#$%^&*()].*")) {
            throw new CustomException("Mật khẩu phải chứa ký tự đặc biệt");
        }

        AccountEntity account = new AccountEntity();
        account.setEmail(registerReq.getEmail());
        account.setPassword(passwordEncoder.encode(registerReq.getPassword()));
        account.setIsVerify(false);
        accountService.create(account);
    }

    public AccountEntity getCurrentAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails user = (UserDetails) principal;
                String email = user.getUsername();
                return accountService.getActiveAccountByEmail(email);
            }
        }
        return null;
    }

    public Long getCurrentAccountId() {
        return getCurrentAccount().getAccountId();
    }

    @Transactional
    public AccountProfileRes updateAccount(UpdateReq updateReq) {
        AccountEntity account = getCurrentAccount();
        if (account == null) {
            throw new CustomException("Tài khoản không tồn tại");
        }

        AccountUpdateReq accountUpdateReq = new AccountUpdateReq();

        if (!updateReq.getFullname().equals(account.getFullname())) {
            accountUpdateReq.setFullname(updateReq.getFullname());
        }

        if (updateReq.getOldPassword() != null && !updateReq.getOldPassword().isEmpty()) {
            if (!passwordEncoder.matches(updateReq.getOldPassword(), account.getPassword())) {
                throw new CustomException("Mật khẩu cũ không chính xác");
            }
            if (!updateReq.getNewPassword().equals(updateReq.getConfirmPassword())) {
                throw new CustomException("Mật khẩu mới và xác nhận mật khẩu không khớp.");
            }
            if (updateReq.getNewPassword().length() < 8) {
                throw new CustomException("Mật khẩu mới phải có ít nhất 8 ký tự");
            }
            if (updateReq.getNewPassword().toLowerCase().equals(updateReq.getNewPassword())) {
                throw new CustomException("Mật khẩu mới phải chứa chữ hoa");
            }
            if (updateReq.getNewPassword().toUpperCase().equals(updateReq.getNewPassword())) {
                throw new CustomException("Mật khẩu mới phải chứa chữ thường");
            }
            if (!updateReq.getNewPassword().matches(".*[0-9].*")) {
                throw new CustomException("Mật khẩu mới phải chứa số");
            }
            if (!updateReq.getNewPassword().matches(".*[!@#$%^&*()].*")) {
                throw new CustomException("Mật khẩu mới phải chứa ký tự đặc biệt");
            }
            accountUpdateReq.setPasswordEncoded(passwordEncoder.encode(updateReq.getNewPassword()));
        }

        if (accountUpdateReq.getFullname() == null && accountUpdateReq.getPasswordEncoded() == null) {
            throw new CustomException("Không có thông tin nào được thay đổi.");
        }

        try {
            accountService.update(accountUpdateReq, account.getAccountId());

            AccountProfileRes accountProfileRes = accountService.getProfileByAccount(account);

            return accountProfileRes;
        } catch (Exception e) {
            throw new QueryException("Có lỗi xảy ra khi cập nhật tài khoản.", e);
        }
    }
}
