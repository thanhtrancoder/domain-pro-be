package thanhtrancoder.domain_pro_be.module.dnsConfig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DnsConfigRepository extends JpaRepository<DnsConfigEntity, Long> {
    Page<DnsConfigEntity> findAllByDomainNameIdAndIsDeleted(
            Long domainNameId,
            Boolean isDeleted,
            Pageable pageable
    );

    Boolean existsByDomainNameIdAndHostAndTypeAndIsDeleted(
            Long domainNameId,
            String host,
            DnsType type,
            Boolean isDeleted
    );
}
