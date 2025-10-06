package thanhtrancoder.domain_pro_be.module.domainName;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DomainNameRepository extends JpaRepository<DomainNameEntity, Long> {
    // @formatter:off
    @Query(nativeQuery = true, value = "" +
            "SELECT EXISTS ( " +
                "SELECT 1 " +
                "FROM domain_name dn " +
                "WHERE " +
                    "dn.is_deleted = 0 " +
                    "AND dn.expires_at >= CURDATE() " +
                    "AND dn.domain_name = :domainName " +
                    "AND dn.domain_extend_id = :domainExtendId " +
            ")")
    Integer isAvailableDomain(String domainName, Long domainExtendId);
    // @formatter:on
}
