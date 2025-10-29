package thanhtrancoder.domain_pro_be.module.passwordResets;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thanhtrancoder.domain_pro_be.module.base.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_resets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetsEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "password_resets_id", nullable = false)
    private Long passwordResetsId;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "otp", nullable = false, length = 6)
    private String otp;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "limit_count", nullable = false)
    private Integer limitCount;

    @Column(name = "used", nullable = false)
    private Boolean used;
}
