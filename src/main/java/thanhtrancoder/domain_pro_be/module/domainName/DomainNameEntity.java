package thanhtrancoder.domain_pro_be.module.domainName;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thanhtrancoder.domain_pro_be.module.base.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "domain_name")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomainNameEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "domain_name_id", nullable = false)
    private Long domainNameId;

    @Column(name = "domain_name", nullable = false)
    private String domainName;

    @Column(name = "domain_extend_id", nullable = false)
    private Long domainExtendId;

    @Column(name = "is_auto_renewal", nullable = false)
    private Boolean isAutoRenewal;

    @Column(name = "register_at", nullable = false)
    private LocalDateTime registerAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "is_block", nullable = false)
    private Boolean isBlock;

    @Column(name = "dns_provider", nullable = false, length = 45)
    private String dnsProvider;

    @Column(name = "account_account_id", nullable = false)
    private Long accountId;

    @Column(name = "status", nullable = false)
    private Integer status;
}
