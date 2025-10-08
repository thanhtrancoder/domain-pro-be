package thanhtrancoder.domain_pro_be.module.domainName.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomainNameDashboardRes {
    private Long totalDomainNameActive;
    private Long totalDomainNameExpiring;
    private Long totalDomainNameExpired;
}
