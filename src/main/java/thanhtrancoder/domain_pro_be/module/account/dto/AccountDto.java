package thanhtrancoder.domain_pro_be.module.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Long accountId;
    private String avatar;
    private String fullname;
    private String email;
    private Boolean isVerify;
    private String googleId;
}
