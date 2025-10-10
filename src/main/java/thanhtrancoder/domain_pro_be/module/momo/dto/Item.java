package thanhtrancoder.domain_pro_be.module.momo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String id;
    private String name;
    private String description;
    private Long price;
    private String currency;
    private Integer quantity;
    private Long totalPrice;
}
