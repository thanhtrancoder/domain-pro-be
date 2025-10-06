package thanhtrancoder.domain_pro_be.module.domainExtend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;
import thanhtrancoder.domain_pro_be.module.domainExtend.dto.DomainExtendDto;
import thanhtrancoder.domain_pro_be.security.auth.AuthService;

@RestController
@RequestMapping("/api/domain-extend")
public class DomainExtendController {
    @Autowired
    private DomainExtendService domainExtendService;
    @Autowired
    private AuthService authService;
    @Autowired
    private ResponseCustomService res;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseCustom<DomainExtendEntity>> create(
            @RequestBody DomainExtendDto domainExtendDto) {
        DomainExtendEntity domainExtend = domainExtendService.adminInsert(
                domainExtendDto,
                authService.getCurrentAccountId());
        return res.success("Tạo domain extend thành công.", domainExtend);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseCustom<Page<DomainExtendDto>>> search(
            @RequestParam String domainName,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<DomainExtendDto> result = domainExtendService.searchAvailableDomainExtend(domainName, pageable);

        return res.success("Tìm kiếm thành công.", result);
    }
}
