package thanhtrancoder.domain_pro_be.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import thanhtrancoder.domain_pro_be.module.account.AccountEntity;
import thanhtrancoder.domain_pro_be.module.account.AccountService;

import java.util.Map;

@Service
public class CustomOidcUserService extends OidcUserService {
    @Autowired
    private AccountService accountService;

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
        }

        return oidcUser;
    }
}
