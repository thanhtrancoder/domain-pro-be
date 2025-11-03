package thanhtrancoder.domain_pro_be.module.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;
import thanhtrancoder.domain_pro_be.module.account.dto.AccountProfileRes;
import thanhtrancoder.domain_pro_be.module.account.dto.AccountUpdateReq;
import thanhtrancoder.domain_pro_be.security.auth.AuthService;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthService authService;
    @Autowired
    private ResponseCustomService res;

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseCustom<AccountProfileRes>> getProfile() {
        AccountProfileRes result = accountService.getProfile(authService.getCurrentAccountId());
        return res.success("User information.", result);
    }
}

