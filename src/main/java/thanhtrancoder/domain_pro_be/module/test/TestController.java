package thanhtrancoder.domain_pro_be.module.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;
import thanhtrancoder.domain_pro_be.module.account.AccountEntity;
import thanhtrancoder.domain_pro_be.security.auth.AuthService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private TestService testService;
    @Autowired
    private ResponseCustomService res;
    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<ResponseCustom<TestEntity>> test() {
        TestEntity testEntity = new TestEntity();
        testEntity.setId(1L);
        testEntity.setName("test");
        testEntity.setCreatedAt(LocalDateTime.now());
        testEntity.setCreatedBy(1L);
        testEntity.setUpdatedAt(LocalDateTime.now());
        testEntity.setUpdatedBy(1L);
        testEntity.setIsDeleted(false);

        return res.success("test ok", testEntity);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<TestEntity>> getUser() {
        return res.success("test user ok", null);
    }

    @GetMapping("/get-info")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseCustom<AccountEntity>> getCurrentAccount() {
        AccountEntity accountEntity = authService.getCurrentAccount();

        return res.success("Thông tin tài khoản hiện tại", accountEntity);
    }
}
