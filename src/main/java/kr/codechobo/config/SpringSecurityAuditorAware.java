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

public class SpringSecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Object principal = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .orElseThrow(RuntimeException::new);

        String createdBy = (String) principal;

        if(createdBy.equals("anonymous")) {
            return Optional.empty();
        }

        return Optional.of(createdBy);
    }
}
