package thanhtrancoder.domain_pro_be.module.cart;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;
import thanhtrancoder.domain_pro_be.common.utils.StringUtils;
import thanhtrancoder.domain_pro_be.module.cart.dto.CartDto;
import thanhtrancoder.domain_pro_be.module.cart.dto.CartItemQuery;
import thanhtrancoder.domain_pro_be.module.domainExtend.DomainExtendService;
import thanhtrancoder.domain_pro_be.module.domainExtend.dto.DomainPriceQuery;
import thanhtrancoder.domain_pro_be.module.domainName.DomainNameService;

import java.time.LocalDateTime;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private DomainNameService domainNameService;
    @Autowired
    private DomainExtendService domainExtendService;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public void addDomainToCart(CartDto cartDto, Long createdBy) {
        if (domainNameService.isAvailableDomain(cartDto.getDomainName(), cartDto.getDomainExtendId())) {
            throw new CustomException("Domain has already been registered.");
        }
        String domainNameWithoutAccent = StringUtils.removeAccent(cartDto.getDomainName());
        String domainNameWithoutWhitespace = StringUtils.removeAllWhitespace(domainNameWithoutAccent);
        String domainNameWithoutSpecialCharacters = StringUtils.removeSpecialCharacters(domainNameWithoutWhitespace);
        CartEntity cartExists = cartRepository.findOneByAccountIdAndDomainNameAndDomainExtendIdAndIsDeleted(
                createdBy,
                domainNameWithoutSpecialCharacters,
                cartDto.getDomainExtendId(),
                false
        );
        Integer period = 1;
        if (cartExists != null) {
            period += cartExists.getPeriod();
        }

        try {
            CartEntity cartEntity = modelMapper.map(cartDto, CartEntity.class);
            if (cartExists != null) {
                cartEntity.setCartId(cartExists.getCartId());
            }
            cartEntity.setAccountId(createdBy);
            cartEntity.setDomainName(domainNameWithoutSpecialCharacters);
            cartEntity.setPeriod(period);
            cartEntity.setCreatedAt(LocalDateTime.now());
            cartEntity.setCreatedBy(createdBy);
            cartEntity.setIsDeleted(false);
            cartRepository.save(cartEntity);
        } catch (Exception e) {
            throw new QueryException("An error occurred while adding the domain to the cart.", e);
        }
    }

    public Page<CartDto> getAll(Long accountId, Pageable pageable) {
        Page<CartItemQuery> cartList = cartRepository.getAllByAccount(accountId, pageable);
        return cartList.map(cartItemQuery -> modelMapper.map(cartItemQuery, CartDto.class));
    }

    @Transactional
    public CartDto updateCartItem(CartDto cartDto, Long accountId) {
        if (cartDto.getPeriod() < 1) {
            throw new CustomException("Quantity must be greater than 0.");
        }
        CartEntity cart = cartRepository.findOneByCartIdAndAccountIdAndIsDeleted(
                cartDto.getCartId(),
                accountId,
                false
        );
        if (cart == null) {
            throw new CustomException("Cart item not found.");
        }
        if (cart.getPeriod() != cartDto.getPeriod()) {
            try {
                cart.setPeriod(cartDto.getPeriod());
                cart.setUpdatedBy(accountId);
                cart.setUpdatedAt(LocalDateTime.now());
                cartRepository.save(cart);
            } catch (Exception e) {
                throw new QueryException("An error occurred while updating quantity in the cart.", e);
            }
        }
        DomainPriceQuery domainPriceQuery = domainExtendService.getPrice(
                cart.getDomainExtendId(),
                cart.getPeriod()
        );
        CartDto cartItem = modelMapper.map(cart, CartDto.class);
        cartItem.setDomainExtend(domainPriceQuery.getDomainExtend());
        cartItem.setBasePrice(domainPriceQuery.getBasePrice());
        cartItem.setDiscountPrice(domainPriceQuery.getDiscountPrice());

        return cartItem;
    }

    @Transactional
    public void deleteCartItem(Long cartId, Long accountId) {
        CartEntity cart = cartRepository.findOneByCartIdAndAccountIdAndIsDeleted(
                cartId,
                accountId,
                false
        );
        if (cart == null) {
            throw new CustomException("Cart item not found.");
        }
        try {
            cart.setIsDeleted(true);
            cart.setUpdatedBy(accountId);
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);
        } catch (Exception e) {
            throw new QueryException("An error occurred while removing the domain from the cart.", e);
        }
    }

    @Transactional
    public void deleteCartItem(Long accountId) {
        try {
            cartRepository.deleteCartItemByAccount(accountId);
        } catch (Exception e) {
            throw new QueryException("An error occurred while refreshing the cart.", e);
        }

    }
}

