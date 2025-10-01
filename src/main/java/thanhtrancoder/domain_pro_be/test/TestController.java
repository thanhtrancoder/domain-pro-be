package thanhtrancoder.domain_pro_be.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private TestService testService;

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

        ResponseCustom<TestEntity> responseCustom = new ResponseCustom<>(
                LocalDateTime.now(),
                200,
                "test ok",
                testEntity
        );

        return ResponseEntity.ok(responseCustom);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<TestEntity>> getUser() {
        ResponseCustom<TestEntity> responseCustom = new ResponseCustom<>(
                LocalDateTime.now(),
                200,
                "test user ok",
                null
        );
        return ResponseEntity.ok(responseCustom);
    }
}
