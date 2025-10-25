package thanhtrancoder.domain_pro_be.module.dnsConfig.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DnsConfigMatchReq {
    private Long domainNameId;
    private List<DnsConfigDto> dnsConfigs;
}
