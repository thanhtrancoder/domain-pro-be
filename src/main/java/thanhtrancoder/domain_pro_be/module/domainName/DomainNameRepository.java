package thanhtrancoder.domain_pro_be.module.domainName;

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
}
