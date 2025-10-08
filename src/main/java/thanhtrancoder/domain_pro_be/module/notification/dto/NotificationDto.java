package thanhtrancoder.domain_pro_be.module.notification.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Long notificationId;
    private String title;
    private String content;
    private Integer type;
    private Integer status;
    private Long accountId;
    private Long relatedId;
    private LocalDateTime createdAt;
}
