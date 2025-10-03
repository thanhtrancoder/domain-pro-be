package thanhtrancoder.domain_pro_be.module.paymentBills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentBillService {
    @Autowired
    private PaymentBillRepository paymentBillRepository;
}
