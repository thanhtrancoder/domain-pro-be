package thanhtrancoder.domain_pro_be.module.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.module.account.AccountEntity;
import thanhtrancoder.domain_pro_be.module.email.EmailService;
import thanhtrancoder.domain_pro_be.module.email.EmailTemplate;
import thanhtrancoder.domain_pro_be.module.email.dto.EmailBody;
import thanhtrancoder.domain_pro_be.module.test.dto.TestDto;
import thanhtrancoder.domain_pro_be.security.auth.AuthService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private TestService testService;
    @Autowired
    private ResponseCustomService res;
    @Autowired
    private AuthService authService;
    @Autowired
    private EmailService emailService;

    @GetMapping
    public ResponseEntity<ResponseCustom<TestEntity>> test() {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            throw new CustomException("An error occurred while simulating load.");
        }

        TestEntity testEntity = new TestEntity();
        testEntity.setId(1L);
        testEntity.setName("Test 1 ok");
        testEntity.setCreatedAt(LocalDateTime.now());
        testEntity.setCreatedBy(1L);
        testEntity.setUpdatedAt(LocalDateTime.now());
        testEntity.setUpdatedBy(1L);
        testEntity.setIsDeleted(false);

        return res.success("Test succeeded.", testEntity);
    }

    @PostMapping("/post")
    public ResponseEntity<ResponseCustom<TestDto>> testPost(@RequestBody TestDto testDto) {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            throw new CustomException("An error occurred while simulating load.");
        }

        return res.success("Test succeeded.", testDto);
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

        return res.success("Current account information", accountEntity);
    }

    @GetMapping("/send")
    public ResponseEntity<String> sendMail() {
        Map<String, Integer> domains = new HashMap<>();
        domains.put("mydomain.com", 1);
        domains.put("mydomain.pro", 2);

        emailService.orderPaymentSuccess(
                5L,

                "123456",
                domains
        );

        return ResponseEntity.ok("Sent");
    }
}
