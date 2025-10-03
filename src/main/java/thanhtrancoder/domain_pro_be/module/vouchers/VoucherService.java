package thanhtrancoder.domain_pro_be.module.vouchers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;
}
