package kr.codechobo.config;

import kr.codechobo.account.AccountService;
import kr.codechobo.api.request.JoinAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/26
 */

@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

    private final AccountService accountService;

    @Override
    public SecurityContext createSecurityContext(WithAccount annotation) {
        String nickname = annotation.value();

        String email = nickname + "@email.com";
        String password = "11111111";
        JoinAccountRequest joinAccountRequest = new JoinAccountRequest(email, nickname, password, password);
        accountService.join(joinAccountRequest);

        UserDetails userDetails = accountService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
