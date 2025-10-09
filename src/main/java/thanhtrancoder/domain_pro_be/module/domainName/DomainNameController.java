package thanhtrancoder.domain_pro_be.module.domainName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;
import thanhtrancoder.domain_pro_be.module.domainName.dto.DomainNameDashboardRes;
import thanhtrancoder.domain_pro_be.module.domainName.dto.DomainNameDto;
import thanhtrancoder.domain_pro_be.security.auth.AuthService;

@RestController
@RequestMapping("/api/domain-name")
public class DomainNameController {
    @Autowired
    private DomainNameService domainNameService;
    @Autowired
    private ResponseCustomService res;
    @Autowired
    private AuthService authService;

    @GetMapping("/count")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<DomainNameDashboardRes>> getCount() {
        DomainNameDashboardRes domainNameDashboardRes = domainNameService.getCount(
                authService.getCurrentAccountId()
        );
        return res.success("Lấy số lượng tên miền thành công.", domainNameDashboardRes);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<Page<DomainNameDto>>> search(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") Integer status,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<DomainNameDto> domainList = domainNameService.search(
                authService.getCurrentAccountId(),
                keyword,
                status,
                pageable
        );
        return res.success("Tìm kiếm tên miền thành công.", domainList);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<DomainNameDto>> update(
            @RequestBody DomainNameDto domainNameDto
    ) {
        DomainNameDto domainNameUpdate = domainNameService.update(
                authService.getCurrentAccountId(),
                domainNameDto
        );
        return res.success("Cập nhật tên miền thành công.", domainNameUpdate);
    }

    @GetMapping("/detail")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCustom<DomainNameDto>> getDetail(
            @RequestParam Long domainNameId
    ) {
        DomainNameDto domainNameDto = domainNameService.getDetail(
                authService.getCurrentAccountId(),
                domainNameId
        );
        return res.success("Lấy thông tin tên miền thành công.", domainNameDto);
    }
}
