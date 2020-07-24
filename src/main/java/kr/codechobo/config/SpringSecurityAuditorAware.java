package kr.codechobo.config;

import kr.codechobo.domain.Account;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/25
 */

public class SpringSecurityAuditorAware implements AuditorAware<Account> {
    @Override
    public Optional<Account> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(Account.class::cast);
    }
}
