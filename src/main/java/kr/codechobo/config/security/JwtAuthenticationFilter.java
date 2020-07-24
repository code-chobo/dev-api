package kr.codechobo.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.codechobo.account.AccountAdapter;
import kr.codechobo.api.request.AuthRequest;
import kr.codechobo.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/17
 */

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final TokenManager tokenManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, TokenManager tokenManager) {
        super.setAuthenticationManager(authenticationManager);
        this.tokenManager = tokenManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token;
        try {
            AuthRequest authRequest = new ObjectMapper().readValue(request.getInputStream(), AuthRequest.class);
            token = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
        } catch (IOException e) {
            throw new BadCredentialsException(e.getMessage());
        }
        setDetails(request, token);
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AccountAdapter adapter = (AccountAdapter) authResult.getPrincipal();

        String token = tokenManager.createToken(adapter.getAccount());

        response.addHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token);
    }


}
