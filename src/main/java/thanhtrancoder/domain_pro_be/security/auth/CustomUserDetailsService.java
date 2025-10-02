package thanhtrancoder.domain_pro_be.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import thanhtrancoder.domain_pro_be.module.account.AccountEntity;
import thanhtrancoder.domain_pro_be.module.account.AccountService;
import thanhtrancoder.domain_pro_be.module.accountRole.AccountRoleService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRoleService accountRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountEntity account = accountService.getAccountByEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<String> roles = accountRoleService.getRolesByAccountId(account.getAccountId());
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        return new User(account.getEmail(), account.getPassword(), authorities);
    }
}
