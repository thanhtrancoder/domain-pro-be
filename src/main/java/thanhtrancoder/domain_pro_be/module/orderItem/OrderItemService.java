package thanhtrancoder.domain_pro_be.module.orderItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;
import thanhtrancoder.domain_pro_be.module.domainExtend.DomainExtendService;

import java.time.LocalDateTime;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private DomainExtendService domainExtendService;

    @Transactional
    public OrderItemEntity createOrderItem(OrderItemEntity orderItemEntity, Long accountId) {

        try {
            orderItemEntity.setCreatedAt(LocalDateTime.now());
            orderItemEntity.setCreatedBy(accountId);
            orderItemEntity.setIsDeleted(false);
            return orderItemRepository.save(orderItemEntity);
        } catch (Exception e) {
            throw new QueryException("Có lỗi xảy ra khi tạo order item.", e);
        }
    }
}
