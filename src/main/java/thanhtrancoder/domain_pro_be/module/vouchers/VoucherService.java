package thanhtrancoder.domain_pro_be.module.vouchers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;
import thanhtrancoder.domain_pro_be.module.vouchers.dto.VoucherApplyRes;
import thanhtrancoder.domain_pro_be.module.vouchers.dto.VoucherDto;

import java.time.LocalDate;


@Service
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private ModelMapper modelMapper;

    public VoucherApplyRes applyVoucher(String code, Long amount) {
        VoucherEntity voucher = voucherRepository.findOneByCodeAndIsDeleted(code, false);
        if (voucher == null) {
            throw new CustomException("Không tìm thấy thông tin voucher.");
        }
        if (voucher.getMinOrderValue() > amount) {
            throw new CustomException("Chưa đạt giá trị đơn hàng tối thiểu để áp dụng voucher.");
        }
        if (voucher.getUsagePerUser() > voucher.getUsageLimit()) {
            throw new CustomException("Voucher đã hết lượt sử dụng.");
        }
        if (voucher.getStartAt().toLocalDate().isAfter(LocalDate.now())) {
            throw new CustomException("Voucher chưa bắt đầu áp dụng.");
        }
        if (voucher.getExpiresAt().toLocalDate().isBefore(LocalDate.now())) {
            throw new CustomException("Voucher đã hết hạn áp dụng.");
        }

        Long discountPriceValue = 0L;
        if (voucher.getDiscountType() == DiscountType.fixed_amount) {
            discountPriceValue = voucher.getDiscountValue();
        } else {
            discountPriceValue = (Long) (amount * voucher.getDiscountValue() / 100);
        }
        if (discountPriceValue > voucher.getMaxDiscountAmount()) {
            discountPriceValue = voucher.getMaxDiscountAmount();
        }

        VoucherApplyRes res = new VoucherApplyRes();
        res.setDiscountPriceValue(discountPriceValue);
        return res;
    }

    public VoucherDto getDiscountest() {
        VoucherEntity voucher = voucherRepository.getDiscountest();
        return modelMapper.map(voucher, VoucherDto.class);
    }

    @Transactional
    public void updateUsage(String code) {
        VoucherEntity voucher = voucherRepository.findOneByCodeAndIsDeleted(code, false);
        if (voucher == null) {
            throw new CustomException("Không tìm thấy thông tin voucher.");
        }
        try {
            voucher.setUsagePerUser(voucher.getUsagePerUser() + 1);
            voucherRepository.save(voucher);
        } catch (Exception e) {
            throw new QueryException("Có lỗi xảy ra trong quá trình cập nhật voucher.", e);
        }

    }
}
