package thanhtrancoder.domain_pro_be.accountRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRoleId implements Serializable {
    private Long accountId;
    private Long roleId;
}
