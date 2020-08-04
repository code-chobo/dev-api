package kr.codechobo.global.config;

import kr.codechobo.domain.account.repository.AccountRepository;
import kr.codechobo.global.util.TokenManager;
import kr.codechobo.global.filter.JwtAuthenticationFilter;
import kr.codechobo.global.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/15
 */

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] POST_EXCLUDE_URL = {"/api/account", "/login"};
    private static final String[] GET_EXCLUDE_URL = {"/api/study/*"};
    private static final String[] EXCLUDE_URL = {"/api/sample"};

    private final TokenManager tokenManager;
    private final AccountRepository accountRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .formLogin().disable()
                .httpBasic().disable();

        http
                .addFilter(authenticationFilter())
                .addFilterAfter(authorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests();

        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, POST_EXCLUDE_URL).permitAll()
                .antMatchers(HttpMethod.GET, GET_EXCLUDE_URL).permitAll()
                .antMatchers(EXCLUDE_URL).permitAll()
                .anyRequest().authenticated();

        http
                .headers().httpStrictTransportSecurity().disable();
    }

    @Bean
    public UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager(), tokenManager);
    }

    @Bean
    public BasicAuthenticationFilter authorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManager(), accountRepository, tokenManager);
    }

}
