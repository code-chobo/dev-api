package kr.codechobo.global.filter;

import kr.codechobo.domain.account.entity.AccountAdapter;
import kr.codechobo.domain.account.exception.AccountNotFoundException;
import kr.codechobo.domain.account.repository.AccountRepository;
import kr.codechobo.domain.account.entity.Account;
import kr.codechobo.global.util.TokenManager;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/19
 */

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final AccountRepository accountRepository;
    private final TokenManager tokenManager;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, AccountRepository accountRepository, TokenManager tokenManager) {
        super(authenticationManager);
        this.accountRepository = accountRepository;
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(token == null || !token.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request,response);
            return;
        }
        Long accountId = tokenManager.extractAccountIdFromToken(token);
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
        UsernamePasswordAuthenticationToken usernamePasswordToken = new UsernamePasswordAuthenticationToken(new AccountAdapter(account), account.getPassword(), List.of(new SimpleGrantedAuthority(account.getRole().toString())));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordToken);

        chain.doFilter(request, response);
    }


}
