package thanhtrancoder.domain_pro_be.module.vouchers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;
import thanhtrancoder.domain_pro_be.module.vouchers.dto.VoucherApplyRes;
import thanhtrancoder.domain_pro_be.module.vouchers.dto.VoucherDto;
import thanhtrancoder.domain_pro_be.security.auth.AuthService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private AuthService authService;
    @Autowired
    private ResponseCustomService res;

    @GetMapping("/apply")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseCustom<VoucherApplyRes>> applyVoucher(
            @RequestParam String code,
            @RequestParam Long amount
    ) {
        VoucherApplyRes voucherApplyRes = voucherService.applyVoucher(code, amount);
        return res.success("Voucher applied successfully.", voucherApplyRes);
    }

    @GetMapping("/discountest")
    public ResponseEntity<ResponseCustom<VoucherDto>> applyVoucher() {
        VoucherDto voucherDto = voucherService.getDiscountest();
        return res.success("Best discount.", voucherDto);
    }
}
