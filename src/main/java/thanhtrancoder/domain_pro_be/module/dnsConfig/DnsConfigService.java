package thanhtrancoder.domain_pro_be.module.dnsConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DnsConfigService {
    @Autowired
    private DnsConfigRepository dnsConfigRepository;
}
