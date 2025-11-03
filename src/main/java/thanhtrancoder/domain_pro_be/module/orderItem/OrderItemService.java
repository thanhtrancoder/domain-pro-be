package thanhtrancoder.domain_pro_be.module.orderItem;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;
import thanhtrancoder.domain_pro_be.module.domainExtend.DomainExtendService;
import thanhtrancoder.domain_pro_be.module.orderItem.dto.OrderItemDto;

import java.time.LocalDateTime;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private DomainExtendService domainExtendService;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public OrderItemEntity createOrderItem(OrderItemEntity orderItemEntity, Long accountId) {
        if (orderItemRepository.existsByOrderIdAndDomainNameAndIsDeleted(
                orderItemEntity.getOrderId(),
                orderItemEntity.getDomainName(),
                false
        )) {
            throw new CustomException("Domain already exists");
        }

        try {
            orderItemEntity.setCreatedAt(LocalDateTime.now());
            orderItemEntity.setCreatedBy(accountId);
            orderItemEntity.setIsDeleted(false);
            return orderItemRepository.save(orderItemEntity);
        } catch (Exception e) {
            throw new QueryException("An error occurred while creating the order item.", e);
        }
    }

    public Page<OrderItemDto> getAllByOrderId(Long orderId, Pageable pageable) {
        Page<OrderItemEntity> orderItemList = orderItemRepository.findAllByOrderIdAndIsDeleted(
                orderId,
                false,
                pageable
        );
        return orderItemList
                .map(orderItem -> modelMapper.map(orderItem, OrderItemDto.class));
    }
}

