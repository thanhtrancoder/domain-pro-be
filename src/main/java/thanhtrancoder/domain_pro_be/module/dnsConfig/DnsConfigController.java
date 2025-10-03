package thanhtrancoder.domain_pro_be.module.dnsConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dns-config")
public class DnsConfigController {
    @Autowired
    private DnsConfigService dnsConfigService;
}
