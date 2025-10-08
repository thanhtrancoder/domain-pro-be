package thanhtrancoder.domain_pro_be.module.domainName.dto;

public interface DomainNameDashboardQuery {
    Long getTotalDomainNameActive();

    Long getTotalDomainNameExpiring();

    Long getTotalDomainNameExpired();
}
