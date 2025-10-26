package thanhtrancoder.domain_pro_be.module.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateReq {
    private String fullname;
    private String passwordEncoded;
}
