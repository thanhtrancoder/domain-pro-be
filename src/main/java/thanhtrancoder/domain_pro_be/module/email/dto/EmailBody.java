package thanhtrancoder.domain_pro_be.module.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailBody {
    private String subject;
    private String body;
}
