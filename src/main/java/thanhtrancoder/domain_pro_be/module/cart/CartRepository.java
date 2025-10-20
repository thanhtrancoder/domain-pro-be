package thanhtrancoder.domain_pro_be.module.cart;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import thanhtrancoder.domain_pro_be.module.cart.dto.CartItemQuery;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    CartEntity findOneByAccountIdAndDomainNameAndDomainExtendIdAndIsDeleted(
            Long accountId,
            String domainName,
            Long domainExtendId,
            Boolean isDeleted
    );

    Page<CartEntity> findAllByAccountIdAndIsDeleted(Long accountId, Boolean isDeleted, Pageable pageable);

    /*
    cart JOIN domain_extend -> get base_price -> price
    min(period - number_of_year) -> discount
    price * (1 - discount) -> discount_price
    */
    // @formatter:off
    @Query(nativeQuery = true, value = "" +
            "SELECT " +
                "c.cart_id, " +
                "c.account_id, " +
                "c.domain_name, " +
                "c.domain_extend_id, " +
                "c.period, " +
                "de.name AS domain_extend, " +
                "de.base_price, " +
                "de.base_price * ( " +
                    "SELECT (1 - discount_price_table.discount) " +
                    "FROM ( " +
                        "SELECT " +
                            "dp.discount, " +
                            "dp.number_of_year, " +
                            "(c.period - dp.number_of_year) AS closest_year " +
                        "FROM discount_price dp " +
                        "WHERE " +
                            "dp.is_deleted = 0 " +
                            "AND dp.domain_extend_id = '1' " +
                            "AND c.period >= dp.number_of_year " +
                        "ORDER BY closest_year ASC " +
                        "LIMIT 1 " +
                    ") AS discount_price_table " +
                ") AS discount_price , " +
                "CASE " +
                    "WHEN dn.domain_name_id IS NULL THEN TRUE " +
                    "ELSE FALSE " +
                "END AS is_available " +
            "FROM cart c " +
                "JOIN domain_extend de ON de.domain_extend_id = c.domain_extend_id " +
                "LEFT JOIN domain_name dn " +
                    "ON dn.domain_extend_id = de.domain_extend_id " +
                    "AND dn.is_deleted = 0 " +
                    "AND dn.domain_name = c.domain_name " +
                    "AND DATE(dn.register_at) <= CURDATE() " +
                    "AND DATE(dn.expires_at) >= CURDATE() " +
            "WHERE " +
                "c.account_id = :accountId " +
                "AND c.is_deleted = 0 " +
                "AND de.is_deleted = 0")
    Page<CartItemQuery> getAllByAccount(Long accountId, Pageable pageable);
    // @formatter:on

    CartEntity findOneByCartIdAndAccountIdAndIsDeleted(Long cartId, Long accountId, Boolean isDeleted);

    // @formatter:off
    @Query(nativeQuery = true, value = "" +
            "UPDATE cart " +
            "SET " +
                "is_deleted = 1, " +
                "updated_at = NOW(), " +
                "updated_by = 0 " +
            "WHERE " +
                "account_id = :accountId " +
                "AND is_deleted = 0")
    void deleteCartItemByAccount(Long accountId);
    // @formatter:on
}
