package thanhtrancoder.domain_pro_be.module.domainName;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import thanhtrancoder.domain_pro_be.module.domainName.dto.DomainNameDashboardQuery;

public interface DomainNameRepository extends JpaRepository<DomainNameEntity, Long> {
    // @formatter:off
    @Query(nativeQuery = true, value = "" +
            "SELECT EXISTS ( " +
                "SELECT 1 " +
                "FROM domain_name dn " +
                "WHERE " +
                    "dn.is_deleted = 0 " +
                    "AND DATE(dn.register_at) <= CURDATE()" +
                    "AND DATE(dn.expires_at) >= CURDATE() " +
                    "AND dn.domain_name = :domainName " +
                    "AND dn.domain_extend_id = :domainExtendId " +
            ")")
    Integer isAvailableDomain(String domainName, Long domainExtendId);
    // @formatter:on

    // @formatter:off
    @Query(nativeQuery = true, value = "" +
            "SELECT " +
                "COUNT( " +
                    "CASE " +
                        "WHEN " +
                            "DATE(dn.register_at) <= CURDATE() " +
                            "AND DATE(dn.expires_at) >= CURDATE() " +
                        "THEN 1 " +
                    "END " +
                ") AS total_domain_name_active, " +
                "COUNT( " +
                    "CASE " +
                        "WHEN " +
                            "DATE(dn.register_at) <= CURDATE() " +
                            "AND DATE(dn.expires_at) >= CURDATE() " +
                            "AND DATE(dn.expires_at) <= DATE_ADD(CURDATE(), INTERVAL 30 DAY) " +
                        "THEN 1 " +
                    "END " +
                ") AS total_domain_name_expiring, " +
                "COUNT( " +
                    "CASE " +
                        "WHEN " +
                            "DATE(dn.expires_at) < CURDATE() " +
                        "THEN 1 " +
                    "END " +
                ") AS total_domain_name_expired " +
            "FROM domain_name dn " +
            "WHERE " +
                "dn.is_deleted = 0 " +
                "AND dn.account_id = :accountId")
    DomainNameDashboardQuery getCount(Long accountId);
    // @formatter:on

    // @formatter:off
    @Query(nativeQuery = true, value = "" +
            "SELECT " +
                "dn.domain_name_id," +
                "dn.created_at, " +
                "dn.created_by, " +
                "dn.is_deleted, " +
                "dn.updated_at, " +
                "dn.updated_by, " +
                "dn.account_id, " +
                "dn.dns_provider, " +
                "dn.domain_extend_id, " +
                "dn.domain_name, " +
                "dn.expires_at, " +
                "dn.is_auto_renewal, " +
                "dn.is_block, " +
                "dn.register_at, " +
                "CASE " +
                    "WHEN " +
                        "DATE(dn.register_at) <= CURDATE() " +
                        "AND DATE(dn.expires_at) >= CURDATE() " +
                    "THEN " +
                        "CASE " +
                            "WHEN DATE(dn.expires_at) <= DATE_ADD(CURDATE(), INTERVAL 30 DAY) " +
                            "THEN 2 " +
                            "ELSE 1 " +
                        "END " +
                    "ELSE 3 " +
                "END AS status, " +
                "dn.domain_extend " +
            "FROM domain_name dn " +
            "WHERE " +
                "dn.is_deleted = 0 " +
                "AND dn.account_id = :accountId " +
                "AND CONCAT(dn.domain_name, dn.domain_extend) LIKE CONCAT('%', :keyword, '%') " +
                "AND " +
                    "CASE " +
                        "WHEN :status = 1 " +
                        "THEN " +
                            "DATE(dn.register_at) <= CURDATE() " +
                            "AND DATE(dn.expires_at) >= CURDATE() " +
                        "WHEN :status = 2 " +
                        "THEN " +
                            "DATE(dn.register_at) <= CURDATE() " +
                            "AND DATE(dn.expires_at) >= CURDATE() " +
                            "AND DATE(dn.expires_at) <= DATE_ADD(CURDATE(), INTERVAL 30 DAY) " +
                        "WHEN :status = 3 " +
                        "THEN DATE(dn.expires_at) < CURDATE() " +
                        "ELSE 1 = 1 " +
                    "END")
    Page<DomainNameEntity> search(Long accountId, String keyword, Integer status, Pageable pageable);
    // @formatter:on

    DomainNameEntity findOneByDomainNameIdAndAccountIdAndIsDeleted(
            Long domainNameId,
            Long accountId,
            Boolean isDeleted
    );

    // @formatter:off
    @Query(nativeQuery = true, value = "" +
            "SELECT " +
                "dn.domain_name_id, " +
                "dn.created_at, " +
                "dn.created_by, " +
                "dn.is_deleted, " +
                "dn.updated_at, " +
                "dn.updated_by, " +
                "dn.account_id, " +
                "dn.dns_provider, " +
                "dn.domain_extend_id, " +
                "dn.domain_name, " +
                "dn.expires_at, " +
                "dn.is_auto_renewal, " +
                "dn.is_block, " +
                "dn.register_at, " +
                "CASE " +
                    "WHEN " +
                        "DATE(dn.register_at) <= CURDATE() " +
                        "AND DATE(dn.expires_at) >= CURDATE() " +
                    "THEN " +
                        "CASE " +
                            "WHEN DATE(dn.expires_at) <= DATE_ADD(CURDATE(), INTERVAL 30 DAY) " +
                            "THEN 2 " +
                            "ELSE 1 " +
                        "END " +
                    "ELSE 3 " +
                "END AS status, " +
                "dn.domain_extend " +
            "FROM domain_name dn " +
            "WHERE " +
                "dn.is_deleted = 0 " +
                "AND dn.account_id = :accountId " +
                "AND dn.domain_name_id = :domainNameId " +
            "LIMIT 1")
    DomainNameEntity getDetail(Long domainNameId, Long accountId);
    // @formatter:on

    Boolean existsByDomainNameIdAndAccountIdAndIsDeleted(
            Long domainNameId,
            Long accountId,
            Boolean isDeleted
    );
}
