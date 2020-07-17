package kr.codechobo.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.codechobo.account.SessionAccount;
import kr.codechobo.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
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

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token;
        try {
            SessionAccount sessionAccount = new ObjectMapper().readValue(request.getInputStream(), SessionAccount.class);
            Account account = sessionAccount.getAccount();
            token = new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setDetails(request, token);
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

    }
}
