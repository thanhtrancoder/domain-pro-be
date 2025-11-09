package thanhtrancoder.domain_pro_be.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import thanhtrancoder.domain_pro_be.common.utils.ConstantValue;
import thanhtrancoder.domain_pro_be.common.utils.StringUtils;
import thanhtrancoder.domain_pro_be.module.account.AccountEntity;
import thanhtrancoder.domain_pro_be.module.account.AccountService;
import thanhtrancoder.domain_pro_be.module.domainName.DomainNameService;
import thanhtrancoder.domain_pro_be.module.domainName.dto.DomainNameDto;
import thanhtrancoder.domain_pro_be.module.email.EmailService;
import thanhtrancoder.domain_pro_be.module.notification.NotificationService;
import thanhtrancoder.domain_pro_be.module.notification.dto.NotificationDto;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CustomOidcUserService extends OidcUserService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private DomainNameService domainNameService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        return processOidcUser(userRequest, oidcUser);
    }

    private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) {
        Map<String, Object> attrs = oidcUser.getAttributes();
        String id = (String) attrs.get("sub");
        String email = (String) attrs.get("email");
        String name = (String) attrs.get("name");
        String avatarUrl = (String) attrs.get("picture");

        if (!accountService.existsByGoogleId(id)) {
            AccountEntity account = new AccountEntity();
            account.setGoogleId(id);
            account.setEmail(email);
            account.setFullname(name);
            account.setAvatar(avatarUrl);
            account.setIsVerify(true);
            accountService.create(account);

            // Create demo DomainName
            DomainNameDto domainNameDto = new DomainNameDto();
            String domainName = "demo" + StringUtils.getEmailUsername(account.getEmail());
            Integer extendDomain = 1;
            while (domainNameService.isAvailableDomain(domainName, 7L)) {
                domainName = "demo" + StringUtils.getEmailUsername(account.getEmail()) + extendDomain;
                extendDomain++;
            }
            domainNameDto.setDomainName(domainName);
            domainNameDto.setDomainExtend(".org");
            domainNameDto.setDomainExtendId(7L);
            domainNameDto.setIsAutoRenewal(false);
            domainNameDto.setRegisterAt(LocalDateTime.now());
            domainNameDto.setExpiresAt(LocalDateTime.now().plusYears(1L));
            domainNameDto.setIsBlock(false);
            domainNameDto.setDnsProvider("CloudDNS");
            domainNameService.create(domainNameDto, account.getAccountId());

            // Send email
            emailService.registrationSuccess(account.getAccountId());

            // Create notification in app
            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setType(ConstantValue.NOTIFICATION_TYPE_SUCCESS);
            notificationDto.setTitle("Account has been created");
            notificationDto.setContent("Notification of successful account creation");
            notificationService.systemCreate(account.getAccountId(), notificationDto);
        }

        return oidcUser;
    }
}
