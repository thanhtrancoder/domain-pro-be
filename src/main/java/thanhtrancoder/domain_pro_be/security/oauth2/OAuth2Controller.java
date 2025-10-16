package thanhtrancoder.domain_pro_be.security.oauth2;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.module.account.AccountEntity;
import thanhtrancoder.domain_pro_be.module.account.AccountService;
import thanhtrancoder.domain_pro_be.security.auth.dto.LoginRes;
import thanhtrancoder.domain_pro_be.security.jwt.JwtUtil;

@RestController
@RequestMapping("/api/oauth2")
public class OAuth2Controller {
    @Autowired
    private ResponseCustomService res;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AccountService accountService;

    private String extractTokenFromCookies(HttpServletRequest req) {
        if (req.getCookies() == null) return null;
        for (Cookie c : req.getCookies()) {
            if ("JWT".equals(c.getName())) return c.getValue();
        }
        return null;
    }

    @GetMapping("/success")
    public String success() {
        return "oauth2 login ok";
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseCustom<LoginRes>> me(HttpServletRequest req) {
        String token = extractTokenFromCookies(req);
        if (token == null) {
            throw new CustomException("Không tìm thấy thông tin đăng nhập.");
        }
        if (!jwtUtil.validateToken(token)) {
            throw new CustomException("Phiên đăng nhập đã hết hạn.");
        }
        String id = jwtUtil.extractEmail(token);

        AccountEntity account = accountService.getAccountByGoogleId(id);
        if (account.getIsDeleted() == true) {
            throw new CustomException("Tài khoản đã bị chặn.");
        }

        String tokenNew = jwtUtil.generateToken(account.getEmail());

        String email = jwtUtil.extractEmail(tokenNew);

        LoginRes loginRes = new LoginRes();
        loginRes.setToken(tokenNew);
        loginRes.setFullname(account.getFullname());
        loginRes.setEmail(account.getEmail());

        return res.success("Đăng nhập thành công.", loginRes);
    }
}
