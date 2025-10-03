package thanhtrancoder.domain_pro_be.module.discountPrice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountPriceService {
    @Autowired
    private DiscountPriceRepository discountPriceRepository;
}
