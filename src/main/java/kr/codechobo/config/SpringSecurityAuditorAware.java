package kr.codechobo.config;

import kr.codechobo.account.AccountAdapter;
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
        Object principal = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .orElseThrow(RuntimeException::new);

        if(principal instanceof String) {
            return Optional.empty();
        }

        AccountAdapter adapter = (AccountAdapter) principal;
        return Optional.of(adapter.getAccount());
    }
}
