package kr.codechobo.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/15
 */

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] POST_EXCLUDE_URL = {"/api/account", "/login"};
    private static final String[] EXCLUDE_URL = {};

    private final TokenManager tokenManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .formLogin().disable();

        http
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), tokenManager));

        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, POST_EXCLUDE_URL).permitAll()
                .anyRequest().authenticated();
    }

}
