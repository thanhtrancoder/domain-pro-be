package thanhtrancoder.domain_pro_be.module.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;
import thanhtrancoder.domain_pro_be.module.notification.dto.NotificationDto;
import thanhtrancoder.domain_pro_be.module.notification.dto.NotificationNewsReq;
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
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<NotificationDto> notificationDtoList = notificationService.getAll(
                authService.getCurrentAccountId(),
                pageable
        );
        return res.success("Fetched notifications successfully.", notificationDtoList);
    }

    @PostMapping("/news/register")
    public ResponseEntity<ResponseCustom<Page<Object>>> registerNews(
            @RequestBody NotificationNewsReq notificationNewsReq
    ) {
        notificationService.registerNews(notificationNewsReq);
        return res.success("Successfully subscribed to news updates.", null);
    }
}

