package thanhtrancoder.domain_pro_be.module.domainExtend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomainExtendDto {
    private Long domainExtendId;
    private String name;
    private Integer basePrice;
}
