package thanhtrancoder.domain_pro_be.module.passwordResets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetsDto {
    private Long passwordResetsId;
    private Long accountId;
    private String token;
    private LocalDateTime expiresAt;
    private Boolean used;
}
