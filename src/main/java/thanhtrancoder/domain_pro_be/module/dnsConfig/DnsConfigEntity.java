package thanhtrancoder.domain_pro_be.module.dnsConfig;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thanhtrancoder.domain_pro_be.module.base.BaseEntity;

@Entity
@Table(name = "dns_config")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DnsConfigEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dns_config_id", nullable = false)
    private Long dnsConfigId;

    @Column(name = "domain_name_id", nullable = false)
    private Long domainNameId;

    @Column(name = "host", nullable = false)
    private String host;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 45)
    private DnsType type;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "ttl", nullable = false)
    private Integer ttl;
}
