package thanhtrancoder.domain_pro_be.module.momo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionLinkRequest {
    private long amount;
    private String orderId;
    private String orderInfo;
    private String extraData;
    private String lang;
    private List<Item> items;
    private UserInfo userInfo;
    private String signature;
}
