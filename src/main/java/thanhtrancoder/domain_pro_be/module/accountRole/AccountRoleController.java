package thanhtrancoder.domain_pro_be.module.accountRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account-role")
public class AccountRoleController {
    @Autowired
    private AccountRoleService accountRoleService;
}
