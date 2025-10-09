package thanhtrancoder.domain_pro_be.module.momo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thanhtrancoder.domain_pro_be.module.momo.dto.CollectionLinkRequest;
import thanhtrancoder.domain_pro_be.module.momo.dto.CollectionLinkResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/momo")
public class MoMoController {
    @Autowired
    private MoMoService momoService;

    @PostMapping("/create")
    public ResponseEntity<CollectionLinkResponse> createLink(@RequestBody CollectionLinkRequest req) {
        CollectionLinkResponse resp = momoService.createCollectionLink(req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/ipn")
    public ResponseEntity<String> handleIpn(@RequestBody Map<String, Object> ipnPayload) {
        momoService.handleIpn(ipnPayload);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/redirect")
    public ResponseEntity<String> redirectPage(@RequestParam Map<String, String> params) {

        return ResponseEntity.ok("Payment status page");
    }
}
