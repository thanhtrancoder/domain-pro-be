package thanhtrancoder.domain_pro_be.module.dnsConfig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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

    List<DnsConfigEntity> findAllByDomainNameIdAndHostAndTypeAndIsDeleted(
            Long domainNameId,
            String host,
            DnsType type,
            Boolean isDeleted
    );

    DnsConfigEntity findOneByDnsConfigIdAndIsDeleted(
            Long dnsConfigId,
            Boolean isDeleted
    );

    // @formatter:off
    @Query(nativeQuery = true, value = "" +
            "SELECT " +
                "CASE " +
                    "WHEN EXISTS ( " +
                        "SELECT 1 " +
                        "FROM dns_config dc " +
                            "JOIN domain_name dn ON dn.domain_name_id = dc.domain_name_id " +
                        "WHERE " +
                            "dc.is_deleted = 0 " +
                            "AND dc.dns_config_id = :dnsConfigId " +
                            "AND dn.is_deleted = 0 " +
                            "AND dn.account_id = :accountId " +
                    ") THEN 1 ELSE 0 " +
                "END ")
    Integer checkAccount(Long accountId, Long dnsConfigId);
    // @formatter:on
}
