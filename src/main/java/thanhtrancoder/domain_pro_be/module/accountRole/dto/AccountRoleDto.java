package thanhtrancoder.domain_pro_be.module.accountRole.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRoleDto {
    private Long accountId;
    private Long roleId;
}
