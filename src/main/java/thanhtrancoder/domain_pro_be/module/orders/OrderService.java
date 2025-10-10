package thanhtrancoder.domain_pro_be.module.orders;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;
import thanhtrancoder.domain_pro_be.common.utils.RegexUtils;
import thanhtrancoder.domain_pro_be.common.utils.ConstantValue;
import thanhtrancoder.domain_pro_be.module.cart.CartService;
import thanhtrancoder.domain_pro_be.module.cart.dto.CartDto;
import thanhtrancoder.domain_pro_be.module.domainExtend.DomainExtendService;
import thanhtrancoder.domain_pro_be.module.orderItem.OrderItemEntity;
import thanhtrancoder.domain_pro_be.module.orderItem.OrderItemService;
import thanhtrancoder.domain_pro_be.module.orders.dto.OrderCreateRes;
import thanhtrancoder.domain_pro_be.module.orders.dto.OrderDto;
import thanhtrancoder.domain_pro_be.module.paymentMethod.PaymentMethodEntity;
import thanhtrancoder.domain_pro_be.module.paymentMethod.PaymentMethodService;
import thanhtrancoder.domain_pro_be.module.vouchers.VoucherService;
import thanhtrancoder.domain_pro_be.module.vouchers.dto.VoucherApplyRes;

import java.time.LocalDateTime;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DomainExtendService domainExtendService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private PaymentMethodService paymentMethodService;

    @Transactional
    public OrderCreateRes createOrder(OrderDto orderDto, Long accountId) {
        if (orderDto.getFullname() == null
                || orderDto.getFullname().trim().isEmpty()) {
            throw new CustomException("Tên khách hàng không được để trống.");
        }
        if (orderDto.getEmail() == null
                || orderDto.getEmail().trim().isEmpty()
                || !RegexUtils.EMAIL_REGEX.matcher(orderDto.getEmail().trim()).matches()) {
            throw new CustomException("Email không hợp lệ.");
        }

        try {
            // get paymentMethod
            PaymentMethodEntity paymentMethodEntity = paymentMethodService.getPaymentMethod(
                    orderDto.getPaymentMethodId()
            );

            Long amount = 0L;

            // create temp order
            OrderEntity order = modelMapper.map(orderDto, OrderEntity.class);
            order.setAccountId(accountId);
            order.setStatus(ConstantValue.ORDER_PENDING);
            order.setTotalPrice(0L);
            order.setPaymentMethodName(paymentMethodEntity.getName());
            order.setCreatedAt(LocalDateTime.now());
            order.setCreatedBy(accountId);
            order.setIsDeleted(false);
            orderRepository.save(order);

            // create order item
            Page<CartDto> cartList = cartService.getAll(accountId, Pageable.unpaged());
            for (CartDto cartDto : cartList) {
                if (cartDto.getIsAvailable() == false) {
                    throw new CustomException("Vui lòng xóa tên miền không khả dụng trong giỏ hàng.");
                }

                OrderItemEntity orderItem = new OrderItemEntity();
                orderItem.setOrderId(order.getOrderId());
                orderItem.setDomainName(cartDto.getDomainName());
                orderItem.setDomainExtend(cartDto.getDomainExtend());
                orderItem.setDomainExtendId(cartDto.getDomainExtendId());
                orderItem.setPeriod(cartDto.getPeriod());
                orderItem.setPrice(cartDto.getDiscountPrice().longValue());
                orderItemService.createOrderItem(orderItem, accountId);

                amount += cartDto.getDiscountPrice().longValue();
            }

            order.setTotalPrice(amount);

            // calculate discount price
            if (orderDto.getDiscountCode() != null && !orderDto.getDiscountCode().isEmpty()) {
                VoucherApplyRes voucherApplyRes = voucherService.applyVoucher(orderDto.getDiscountCode(), amount);
                order.setDiscountPrice(voucherApplyRes.getDiscountPriceValue());
                order.setTotalPrice(amount - voucherApplyRes.getDiscountPriceValue());
            }

            orderRepository.save(order);

            return modelMapper.map(order, OrderCreateRes.class);
        } catch (Exception e) {
            if (e instanceof CustomException) {
                throw new CustomException(e.getMessage());
            } else {
                throw new QueryException("Có lỗi xảy ra khi tạo đơn hàng.", e);
            }
        }
    }

    @Transactional
    public OrderDto updateOrderStatus(Long orderId, Integer status, Long accountId) {
        OrderEntity order = orderRepository.findOneByOrderIdAndIsDeleted(orderId, false);
        if (order == null) {
            throw new CustomException("Không tìm thấy thông tin đơn hàng.");
        }
        if (status != ConstantValue.ORDER_PAID) {
            throw new CustomException("Trạng thái đơn hàng không hợp lệ");
        }
        if (order.getStatus() == ConstantValue.ORDER_PAID) {
            throw new CustomException("Không được phép cập nhật trạng thái đơn hàng này.");
        }

        try {
            order.setStatus(status);
            order.setUpdatedAt(LocalDateTime.now());
            order.setUpdatedBy(accountId);
            orderRepository.save(order);
            return modelMapper.map(order, OrderDto.class);
        } catch (Exception e) {
            throw new QueryException("Có lỗi xảy ra khi cập nhật trạng thái đơn hàng.", e);
        }
    }

    public OrderDto getDetail(Long orderId, Long accountId) {
        OrderEntity order = orderRepository.findOneByOrderIdAndAccountIdAndIsDeleted(
                orderId,
                accountId,
                false
        );
        if (order == null) {
            throw new CustomException("Không tìm thấy thông tin đơn hàng.");
        }
        return modelMapper.map(order, OrderDto.class);
    }
}
