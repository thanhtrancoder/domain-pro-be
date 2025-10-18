package thanhtrancoder.domain_pro_be.module.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;
import thanhtrancoder.domain_pro_be.module.cart.dto.CartDto;
import thanhtrancoder.domain_pro_be.security.auth.AuthService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private AuthService authService;
    @Autowired
    private ResponseCustomService res;

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<Object>> addDomainToCart(@RequestBody CartDto cartDto) {
        cartService.addDomainToCart(cartDto, authService.getCurrentAccountId());
        return res.success("Thêm domain vào giỏ hàng thành công.", null);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<Page<CartDto>>> getAll(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<CartDto> cartList = cartService.getAll(authService.getCurrentAccountId(), Pageable.unpaged());
        return res.success("Lấy danh sách sản phẩm trong giỏ hàng thành công.", cartList);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<CartDto>> updateCartItem(@RequestBody CartDto cartDto) {
        CartDto cart = cartService.updateCartItem(cartDto, authService.getCurrentAccountId());
        return res.success("Cập nhật giỏ hàng thành công.", cart);
    }

    @DeleteMapping("/delete/{cartId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<Object>> deleteCartItem(@PathVariable Long cartId) {
        cartService.deleteCartItem(cartId, authService.getCurrentAccountId());
        return res.success("Xóa domain khỏi giỏ hàng thành công.", null);
    }
}
