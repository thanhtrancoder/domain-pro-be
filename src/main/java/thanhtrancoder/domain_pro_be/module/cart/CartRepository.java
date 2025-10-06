package thanhtrancoder.domain_pro_be.module.cart;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import thanhtrancoder.domain_pro_be.module.cart.dto.CartItemQuery;
import thanhtrancoder.domain_pro_be.module.cart.dto.CartPriceQuery;

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
                "c.period * de.base_price AS price, " +
                "c.period * de.base_price * ( " +
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
                    ") AS discount_price_table) " +
                "AS discount_price " +
            "FROM cart c " +
            "JOIN domain_extend de ON c.domain_extend_id = de.domain_extend_id " +
            "WHERE " +
                "c.account_id = :accountId " +
                "AND c.is_deleted = 0")
    Page<CartItemQuery> getAllByAccount(Long accountId, Pageable pageable);
    // @formatter:on

    CartEntity findOneByCartIdAndAccountIdAndIsDeleted(Long cartId, Long accountId, Boolean isDeleted);

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
            "WHERE de.domain_extend_id = :domainExtendId " +
                "AND de.is_deleted = 0")
    CartPriceQuery getCartPrice(Long domainExtendId, Integer period);
    // @formatter:on
}
