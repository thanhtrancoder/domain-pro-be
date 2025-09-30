package thanhtrancoder.domain_pro_be.common.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCustom<T> {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private T data;
}
