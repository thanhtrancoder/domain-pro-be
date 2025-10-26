package thanhtrancoder.domain_pro_be.security.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReq {
    private String fullname;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
