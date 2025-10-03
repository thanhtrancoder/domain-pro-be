package thanhtrancoder.domain_pro_be.module.domainExtend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DomainExtendRepository extends JpaRepository<DomainExtendEntity, Long> {
    Boolean existsByNameAndIsDeleted(String name, Boolean isDeleted);

    // @formatter:off
    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM domain_pro.domain_extend de " +
            "WHERE de.domain_extend_id NOT IN ( " +
                "SELECT dn.domain_extend_id " +
                "FROM domain_pro.domain_name dn " +
                "WHERE dn.domain_name = :domainName " +
            ")")
    Page<DomainExtendEntity> findAvailableDomainExtend(String domainName, Pageable pageable);
    // @formatter:on
}
