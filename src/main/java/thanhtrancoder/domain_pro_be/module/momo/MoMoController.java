package thanhtrancoder.domain_pro_be.module.momo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;
import thanhtrancoder.domain_pro_be.module.momo.dto.CheckPaymentRes;
import thanhtrancoder.domain_pro_be.module.momo.dto.CollectionLinkRequest;
import thanhtrancoder.domain_pro_be.module.momo.dto.MoMoReq;
import thanhtrancoder.domain_pro_be.module.momo.dto.MoMoRes;
import thanhtrancoder.domain_pro_be.security.auth.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/momo")
public class MoMoController {
    @Autowired
    private MoMoService momoService;
    @Autowired
    private AuthService authService;
    @Autowired
    private ResponseCustomService res;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<MoMoRes>> createLink(@RequestBody CollectionLinkRequest req) {
        MoMoRes moMoRes = momoService.createCollectionLink(
                req, authService.getCurrentAccountId()
        );

        return res.success("Payment link created successfully.", moMoRes);
    }

    @PostMapping("/ipn")
    public ResponseEntity<String> handleIpn(@RequestBody MoMoReq moMoReq) {
        momoService.handlePayment(moMoReq);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/check")
    public ResponseEntity<ResponseCustom<CheckPaymentRes>> checkPayment(@RequestBody MoMoReq moMoReq) {
        CheckPaymentRes payment = momoService.checkPayment(moMoReq);
        return res.success("Payment successful.", payment);
    }
}

