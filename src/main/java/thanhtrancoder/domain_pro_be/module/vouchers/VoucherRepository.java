package thanhtrancoder.domain_pro_be.module.vouchers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VoucherRepository extends JpaRepository<VoucherEntity, Long> {
    Boolean existsByCodeAndIsDeleted(String voucherCode, Boolean isDeleted);

    VoucherEntity findOneByCodeAndIsDeleted(String voucherCode, Boolean isDeleted);

    // @formatter:off
    @Query(nativeQuery = true, value = "" +
            "SELECT " +
                "LEAST( " +
                    "CASE " +
                        "WHEN v.discount_type = 'fixed_amount' THEN v.discount_value " +
                        "ELSE v.discount_value * :amount / 100 " +
                    "END, " +
                    "v.max_discount_amount " +
                ") AS discount_price_value " +
            "FROM vouchers v " +
            "WHERE " +
                "v.is_deleted = 0 " +
                "AND v.code = :code " +
                "AND v.min_order_value <= :amount " +
                "AND v.usage_per_user < v.usage_limit " +
                "AND DATE(v.start_at) <= CURDATE() " +
                "AND DATE(v.expires_at) >= CURDATE()")
    Long getDiscountPriceValue(String code, Long amount);
    // @formatter:on

    // @formatter:off
    @Query(nativeQuery = true, value = "" +
            "SELECT * " +
            "FROM vouchers v " +
            "WHERE " +
                "v.is_deleted = 0 " +
                "AND DATE(v.start_at) <= CURDATE() " +
                "AND DATE(v.expires_at) >= CURDATE() " +
            "ORDER BY v.max_discount_amount DESC " +
            "LIMIT 1")
    VoucherEntity getDiscountest();
    // @formatter:on
}
