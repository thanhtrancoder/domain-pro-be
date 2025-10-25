package thanhtrancoder.domain_pro_be.module.dnsConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;
import thanhtrancoder.domain_pro_be.module.dnsConfig.dto.DnsConfigDto;
import thanhtrancoder.domain_pro_be.module.dnsConfig.dto.DnsConfigMatchReq;
import thanhtrancoder.domain_pro_be.security.auth.AuthService;

import java.util.List;

@RestController
@RequestMapping("/api/dns-config")
public class DnsConfigController {
    @Autowired
    private DnsConfigService dnsConfigService;
    @Autowired
    private AuthService authService;
    @Autowired
    private ResponseCustomService res;

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<Page<DnsConfigDto>>> getAll(
            @RequestParam Long domainNameId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<DnsConfigDto> dnsConfigList = dnsConfigService.getAll(
                authService.getCurrentAccountId(),
                domainNameId,
                Pageable.unpaged()
        );
        return res.success("Lấy danh sách cấu hình DNS thành công.", dnsConfigList);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<DnsConfigDto>> create(
            @RequestBody DnsConfigDto dnsConfigDto
    ) {
        DnsConfigDto dnsConfigCreate = dnsConfigService.create(
                authService.getCurrentAccountId(),
                dnsConfigDto
        );
        return res.success("Tạo cấu hình DNS thành công.", dnsConfigCreate);
    }

    // update
    @PutMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<DnsConfigDto>> update(
            @RequestBody DnsConfigDto dnsConfigDto
    ) {
        DnsConfigDto dnsConfigUpdate = dnsConfigService.update(
                authService.getCurrentAccountId(),
                dnsConfigDto
        );
        return res.success("Cập nhật cấu hình DNS thành công.", dnsConfigUpdate);
    }

    // delete
    @DeleteMapping("/delete/{dnsConfigId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<Object>> delete(@PathVariable Long dnsConfigId) {
        dnsConfigService.delete(authService.getCurrentAccountId(), dnsConfigId);
        return res.success("Xóa cấu hình DNS thành công.", null);
    }

    @PatchMapping("/match")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<Page<DnsConfigDto>>> match(
            @RequestBody DnsConfigMatchReq req
    ) {
        Page<DnsConfigDto> dnsConfigUpdate = dnsConfigService.match(
                authService.getCurrentAccountId(),
                req
        );
        return res.success("Cập nhật cấu hình DNS thành công.", dnsConfigUpdate);
    }
}
