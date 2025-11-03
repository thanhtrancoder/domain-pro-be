package thanhtrancoder.domain_pro_be.module.paymentBills;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;
import thanhtrancoder.domain_pro_be.module.paymentBills.dto.PaymentBillDto;

import java.time.LocalDateTime;

@Service
public class PaymentBillService {
    @Autowired
    private PaymentBillRepository paymentBillRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public void create(PaymentBillDto paymentBillDto, Long accountId) {
        if (paymentBillRepository.existsByOrderIdAndIsDeleted(
                paymentBillDto.getOrderId(),
                false)
        ) {
            throw new CustomException("This order already has a payment invoice.");
        }
        try {
            PaymentBillEntity paymentBillEntity = modelMapper.map(
                    paymentBillDto,
                    PaymentBillEntity.class
            );
            paymentBillEntity.setCreatedAt(LocalDateTime.now());
            paymentBillEntity.setCreatedBy(accountId);
            paymentBillEntity.setIsDeleted(false);
            paymentBillRepository.save(paymentBillEntity);
        } catch (Exception e) {
            throw new QueryException("An error occurred while creating the payment invoice.", e);
        }
    }

    public PaymentBillDto getDetailByOrderId(String orderId) {
        PaymentBillEntity paymentBillEntity = paymentBillRepository.findOneByOrderIdAndIsDeleted(
                orderId,
                false
        );
        if (paymentBillEntity == null) {
            throw new CustomException("Payment invoice information not found.");
        }
        return modelMapper.map(paymentBillEntity, PaymentBillDto.class);
    }

    public PaymentBillEntity adminGetDetailByOrderId(String orderId) {
        PaymentBillEntity paymentBillEntity = paymentBillRepository.findOneByOrderIdAndIsDeleted(
                orderId,
                false
        );
        if (paymentBillEntity == null) {
            throw new CustomException("Payment invoice information not found.");
        }
        return paymentBillEntity;
    }

    public PaymentBillEntity getDetailByOrderIdOrNull(String orderId) {
        PaymentBillEntity paymentBillEntity = paymentBillRepository.findOneByOrderIdAndIsDeleted(
                orderId,
                false
        );
        return paymentBillEntity;
    }
}

