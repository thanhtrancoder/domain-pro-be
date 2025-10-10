package thanhtrancoder.domain_pro_be.module.orderItem;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thanhtrancoder.domain_pro_be.module.base.BaseEntity;

@Entity
@Table(name = "order_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id", nullable = false)
    private Long orderItemId;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "domain_name", nullable = false)
    private String domainName;

    @Column(name = "domain_extend", nullable = false)
    private String domainExtend;

    @Column(name = "domain_extend_id", nullable = false)
    private Long domainExtendId;

    @Column(name = "period", nullable = false)
    private Integer period;

    @Column(name = "price", nullable = false)
    private Long price;
}
