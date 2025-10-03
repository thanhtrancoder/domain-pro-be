package thanhtrancoder.domain_pro_be.module.discountPrice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/discount-price")
public class DiscountPriceController {
    @Autowired
    private DiscountPriceService discountPriceService;
}
