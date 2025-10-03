package thanhtrancoder.domain_pro_be.module.dnsConfig.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DnsConfigDto {
    private Long dnsConfigId;
    private Long domainNameId;
    private String type; // mapped from enum in entity
    private String domain;
    private String value;
    private Integer ttl;
}
