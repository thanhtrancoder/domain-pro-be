package thanhtrancoder.domain_pro_be.module.domainExtend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import thanhtrancoder.domain_pro_be.module.domainExtend.dto.DomainPriceQuery;

import java.util.List;

public interface DomainExtendRepository extends JpaRepository<DomainExtendEntity, Long> {
    Boolean existsByNameAndIsDeleted(String name, Boolean isDeleted);

    // @formatter:off
    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM domain_extend de " +
            "WHERE " +
                "de.is_deleted = 0 " +
                "AND de.domain_extend_id NOT IN ( " +
                    "SELECT dn.domain_extend_id " +
                    "FROM domain_name dn " +
                    "WHERE dn.is_deleted = 0 AND dn.expires_at >= CURDATE() AND dn.domain_name = :domainName " +
                ")")
    Page<DomainExtendEntity> findAvailableDomainExtend(String domainName, Pageable pageable);
    // @formatter:on

    /*
    FROM domain_extend -> get name, get base_price
        period * base_price -> price
    FROM discount_price(domain_extend_id) -> min(period - number_of_year) -> discount
    price * (1 - discount) -> discount_price
    */
    // @formatter:off
    @Query(nativeQuery = true, value = "" +
            "SELECT " +
                "de.name AS domain_extend, " +
                "de.base_price * :period AS price, " +
                "de.base_price * :period * ( " +
                    "SELECT 1 - discount_price_table.discount " +
                    "FROM ( " +
                        "SELECT " +
                            "dp.discount, " +
                            "dp.number_of_year, " +
                            "(:period - dp.number_of_year) AS closest_year " +
                        "FROM discount_price dp " +
                        "WHERE " +
                            "dp.is_deleted = 0 " +
                            "AND dp.domain_extend_id = :domainExtendId " +
                            "AND :period >= dp.number_of_year " +
                        "ORDER BY closest_year ASC " +
                        "LIMIT 1" +
                    ") AS discount_price_table " +
                ") AS discount_price " +
            "FROM domain_extend de " +
            "WHERE " +
                "de.domain_extend_id = :domainExtendId " +
                "AND de.is_deleted = 0")
    DomainPriceQuery getPrice(Long domainExtendId, Integer period);
    // @formatter:on

    // @formatter:off
    @Query(nativeQuery = true, value = "" +
            "SELECT " +
                "de.*, " +
                "IFNULL(dn_count.number_of_name, 0) AS number_of_name " +
            "FROM domain_extend de " +
                "LEFT JOIN " +
                    "( " +
                        "SELECT " +
                            "dn.domain_extend_id, " +
                            "COUNT(*) AS number_of_name " +
                        "FROM domain_name dn " +
                        "WHERE " +
                            "dn.is_deleted = 0 " +
                            "AND DATE(dn.register_at) <= CURDATE() " +
                            "AND DATE(dn.expires_at) >= CURDATE() " +
                        "GROUP BY dn.domain_extend_id " +
                    ") dn_count ON dn_count.domain_extend_id = de.domain_extend_id " +
            "WHERE de.is_deleted = 0 " +
            "ORDER BY dn_count.number_of_name DESC " +
            "LIMIT 6")
    List<DomainExtendEntity> getPopularDomainExtend();
    // @formatter:on
}
