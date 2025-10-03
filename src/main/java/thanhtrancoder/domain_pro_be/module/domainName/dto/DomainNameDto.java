package thanhtrancoder.domain_pro_be.module.domainName.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomainNameDto {
    private Long domainNameId;
    private String domainName;
    private Long domainExtendId;
    private Boolean isAutoRenewal;
    private LocalDateTime registerAt;
    private LocalDateTime expiresAt;
    private Boolean isBlock;
    private String dnsProvider;
    private Long accountId;
    private Integer status;
}
