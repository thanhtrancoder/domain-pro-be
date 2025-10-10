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
            throw new CustomException("Đơn hàng này đã có hóa đơn thanh toán.");
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
            throw new QueryException("Có lỗi xảy ra khi tạo hóa đơn thanh toán.", e);
        }
    }

    public PaymentBillDto getDetailByOrderId(String orderId) {
        PaymentBillEntity paymentBillEntity = paymentBillRepository.findOneByOrderIdAndIsDeleted(
                orderId,
                false
        );
        if (paymentBillEntity == null) {
            throw new CustomException("Không tìm thấy thông tin hóa đơn thanh toán.");
        }
        return modelMapper.map(paymentBillEntity, PaymentBillDto.class);
    }
}
