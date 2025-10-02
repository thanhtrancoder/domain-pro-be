package thanhtrancoder.domain_pro_be.module.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustomService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private ResponseCustomService response;

    @GetMapping("/all")
    public ResponseEntity<ResponseCustom<Page<RoleEntity>>> getAll(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<RoleEntity> roleList = roleService.getAll(pageable);

        return response.success(
                "",
                roleList
        );
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseCustom<RoleEntity>> create(@RequestBody RoleEntity roleEntity) {
        RoleEntity roleCreated = roleService.create(roleEntity);

        return response.success(
                "Tạo role thành công.",
                roleCreated
        );
    }
}
