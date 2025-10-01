package thanhtrancoder.domain_pro_be.common.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import thanhtrancoder.domain_pro_be.account.AccountEntity;
import thanhtrancoder.domain_pro_be.account.AccountService;
import thanhtrancoder.domain_pro_be.common.auth.dto.LoginReq;
import thanhtrancoder.domain_pro_be.common.auth.dto.RegisterReq;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;

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
        if(!passwordEncoder.matches(loginReq.getPassword(), account.getPassword())) {
            throw new CustomException("Sai tên tài khoản hoặc mật khẩu");
        }
        return account;
    }

    public void register(RegisterReq registerReq) {
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
        accountService.create(account);
    }
}
