package thanhtrancoder.domain_pro_be.module.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;
import thanhtrancoder.domain_pro_be.module.notification.dto.NotificationDto;
import thanhtrancoder.domain_pro_be.security.auth.AuthService;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ResponseCustomService res;
    @Autowired
    private AuthService authService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<Page<NotificationDto>>> getAll(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<NotificationDto> notificationDtoList = notificationService.getAll(
                authService.getCurrentAccountId(),
                pageable
        );
        return res.success("Lấy danh sách thông báo thành công.", notificationDtoList);
    }
}
