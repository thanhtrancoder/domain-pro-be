package thanhtrancoder.domain_pro_be.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thanhtrancoder.domain_pro_be.module.account.AccountEntity;
import thanhtrancoder.domain_pro_be.security.auth.dto.LoginReq;
import thanhtrancoder.domain_pro_be.security.auth.dto.LoginRes;
import thanhtrancoder.domain_pro_be.security.auth.dto.RegisterReq;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.security.jwt.JwtUtil;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseCustom<LoginRes>> login(@RequestBody LoginReq loginReq) {
        AccountEntity accountEntity = authService.login(loginReq);

        String token = jwtUtil.generateToken(loginReq.getEmail());

        LoginRes loginRes = new LoginRes();
        loginRes.setToken(token);
        loginRes.setFullname(accountEntity.getFullname());
        loginRes.setEmail(accountEntity.getEmail());

        ResponseCustom responseCustom = new ResponseCustom();
        responseCustom.setTimestamp(LocalDateTime.now());
        responseCustom.setStatus(200);
        responseCustom.setMessage("Đăng nhập thành công");
        responseCustom.setData(loginRes);

        return ResponseEntity.ok(responseCustom);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseCustom> register(@RequestBody RegisterReq registerReq) {
        authService.register(registerReq);

        ResponseCustom responseCustom = new ResponseCustom();
        responseCustom.setTimestamp(LocalDateTime.now());
        responseCustom.setStatus(200);
        responseCustom.setMessage("Đăng ký thành công");
        responseCustom.setData(null);

        return ResponseEntity.ok(responseCustom);
    }
}
