package thanhtrancoder.domain_pro_be.module.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;
import thanhtrancoder.domain_pro_be.module.orders.dto.OrderCreateRes;
import thanhtrancoder.domain_pro_be.module.orders.dto.OrderDto;
import thanhtrancoder.domain_pro_be.security.auth.AuthService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private AuthService authService;
    @Autowired
    private ResponseCustomService res;

    @PostMapping("create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<OrderCreateRes>> createOrder(@RequestBody OrderDto orderDto) {
        OrderCreateRes orderCreateRes = orderService.createOrder(orderDto, authService.getCurrentAccountId());

        return res.success("Order created successfully.", orderCreateRes);
    }
}

