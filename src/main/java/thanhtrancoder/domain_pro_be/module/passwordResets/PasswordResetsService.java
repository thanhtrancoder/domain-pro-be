package thanhtrancoder.domain_pro_be.module.passwordResets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;
import thanhtrancoder.domain_pro_be.common.utils.StringUtils;

import java.time.LocalDateTime;

@Service
public class PasswordResetsService {
    @Autowired
    private PasswordResetsRepository passwordResetsRepository;

    @Transactional
    public String create(Long accountId) {
        try {
            // Check old Otp
            passwordResetsRepository.cancelAllOldOtp(accountId);

            // Create otp
            String otp = StringUtils.generateNumericCode(6);

            PasswordResetsEntity passwordResetsEntity = new PasswordResetsEntity();
            passwordResetsEntity.setAccountId(accountId);
            passwordResetsEntity.setOtp(otp);
            passwordResetsEntity.setExpiresAt(LocalDateTime.now().plusMinutes(10));
            passwordResetsEntity.setLimitCount(5);
            passwordResetsEntity.setUsed(false);
            passwordResetsEntity.setCreatedAt(LocalDateTime.now());
            passwordResetsEntity.setCreatedBy(0L);
            passwordResetsEntity.setIsDeleted(false);
            passwordResetsRepository.save(passwordResetsEntity);

            return otp;
        } catch (Exception e) {
            throw new QueryException("Có lỗi xảy ra khi tạo password reset.", e);
        }
    }

    @Transactional
    public Boolean apply(Long accountId, String otp) {
        PasswordResetsEntity passwordResetsEntity = passwordResetsRepository.findOneByAccountIdAndUsedAndIsDeleted(
                accountId,
                false,
                false
        );
        if (passwordResetsEntity == null) {
            throw new CustomException("Mã OTP không chính xác.");
        }
        if (passwordResetsEntity.getLimitCount() <= 0) {
            throw new CustomException("Mã OTP không chính xác.");
        }
        if (passwordResetsEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CustomException("Mã OTP không chính xác.");
        }
        if (!passwordResetsEntity.getOtp().equals(otp)) {
            try {
                Integer newLimit = passwordResetsEntity.getLimitCount() - 1;
                passwordResetsEntity.setLimitCount(newLimit);
                passwordResetsEntity.setUpdatedAt(LocalDateTime.now());
                passwordResetsEntity.setUpdatedBy(0L);
                passwordResetsRepository.save(passwordResetsEntity);
                return false;
            } catch (Exception e) {
                throw new QueryException("Có lỗi xảy ra khi áp dụng password reset.", e);
            }
        }

        try {
            passwordResetsEntity.setUsed(true);
            passwordResetsEntity.setUpdatedAt(LocalDateTime.now());
            passwordResetsEntity.setUpdatedBy(0L);
            passwordResetsRepository.save(passwordResetsEntity);
            return true;
        } catch (Exception e) {
            throw new QueryException("Có lỗi xảy ra khi áp dụng password reset.", e);
        }
    }
}
