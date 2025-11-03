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
            throw new CustomException("Voucher not found.");
        }
        if (voucher.getMinOrderValue() > amount) {
            throw new CustomException("Minimum order value not met to apply the voucher.");
        }
        if (voucher.getUsagePerUser() > voucher.getUsageLimit()) {
            throw new CustomException("Voucher usage limit reached.");
        }
        if (voucher.getStartAt().toLocalDate().isAfter(LocalDate.now())) {
            throw new CustomException("Voucher has not started yet.");
        }
        if (voucher.getExpiresAt().toLocalDate().isBefore(LocalDate.now())) {
            throw new CustomException("Voucher has expired.");
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
            throw new CustomException("Voucher not found.");
        }
        try {
            voucher.setUsagePerUser(voucher.getUsagePerUser() + 1);
            voucherRepository.save(voucher);
        } catch (Exception e) {
            throw new QueryException("An error occurred while updating the voucher.", e);
        }

    }
}
