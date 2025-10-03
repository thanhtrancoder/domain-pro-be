package thanhtrancoder.domain_pro_be.module.paymentBills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment-bills")
public class PaymentBillController {
    @Autowired
    private PaymentBillService paymentBillService;
}
