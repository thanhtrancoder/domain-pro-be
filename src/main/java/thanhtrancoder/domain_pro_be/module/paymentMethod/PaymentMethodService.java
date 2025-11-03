package thanhtrancoder.domain_pro_be.module.paymentMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;

import java.util.Optional;

@Service
public class PaymentMethodService {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodEntity getPaymentMethod(Long id) {
        Optional<PaymentMethodEntity> paymentMethodEntityOptional = paymentMethodRepository.findById(id);
        if (paymentMethodEntityOptional.isEmpty()) {
            throw new CustomException("Payment method not found.");
        }
        if (paymentMethodEntityOptional.get().getIsDeleted() == true) {
            throw new CustomException("Payment method not found.");
        }
        return paymentMethodEntityOptional.get();
    }
}
