package thanhtrancoder.domain_pro_be.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;
import thanhtrancoder.domain_pro_be.module.account.AccountEntity;
import thanhtrancoder.domain_pro_be.module.account.dto.AccountProfileRes;
import thanhtrancoder.domain_pro_be.module.account.dto.AccountUpdateReq;
import thanhtrancoder.domain_pro_be.security.auth.dto.*;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.security.jwt.JwtUtil;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private ResponseCustomService res;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseCustom<LoginRes>> login(@RequestBody LoginReq loginReq) {
        AccountEntity accountEntity = authService.login(loginReq);

        String token = jwtUtil.generateToken(loginReq.getEmail(), accountEntity.getTokenVersion());

        LoginRes loginRes = new LoginRes();
        loginRes.setToken(token);
        loginRes.setFullname(accountEntity.getFullname());
        loginRes.setEmail(accountEntity.getEmail());

        return res.success("Login successful.", loginRes);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseCustom<LoginRes>> register(@RequestBody RegisterReq registerReq) {
        authService.register(registerReq);

        LoginReq loginReq = new LoginReq();
        loginReq.setEmail(registerReq.getEmail());
        loginReq.setPassword(registerReq.getPassword());
        AccountEntity accountEntity = authService.login(loginReq);

        String token = jwtUtil.generateToken(loginReq.getEmail(), accountEntity.getTokenVersion());

        LoginRes loginRes = new LoginRes();
        loginRes.setToken(token);
        loginRes.setFullname(accountEntity.getFullname());
        loginRes.setEmail(accountEntity.getEmail());

        return res.success("Registration successful.", loginRes);
    }

    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseCustom<AccountProfileRes>> updateAccount(@RequestBody UpdateReq updateReq) {
        AccountProfileRes result = authService.updateAccount(updateReq);
        return res.success("Account updated successfully.", result);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseCustom<Object>> forgotPassword(@RequestBody ForgotPasswordReq forgotPasswordReq) {
        authService.forgotPassword(forgotPasswordReq.getEmail());
        return res.success("Password reset request submitted successfully.", null);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseCustom<Object>> forgotPassword(@RequestBody ResetPasswordReq resetPasswordReq) {
        Boolean result = authService.resetPassword(resetPasswordReq);
        if (!result) {
            return res.fail("Invalid OTP code.");
        }
        return res.success("Password has been changed.", null);
    }
}

