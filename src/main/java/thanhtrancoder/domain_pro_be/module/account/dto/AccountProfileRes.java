package thanhtrancoder.domain_pro_be.module.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountProfileRes {
    private String fullname;
    private String email;
    private Boolean isVerify;
    private String avatar;
    private List<String> roles;
    private Long numberCartItem;
}
