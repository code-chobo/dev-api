package kr.codechobo.config;

import kr.codechobo.domain.Account;
import kr.codechobo.domain.AccountRole;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/26
 */

@TestConfiguration
@Profile("test")
@EnableJpaAuditing(auditorAwareRef = "testAuditorProvider")
public class TestProfileConfiguration {

    @Bean
    public AuditorAware<String> testAuditorProvider() {

        Account account = Account.builder()
                .nickname("TEST_AUDITOR")
                .email("TEST_AUDITOR@EMAIL.COM")
                .role(AccountRole.COMMON)
                .password("11111111")
                .build();

        return () -> Optional.of(account.getEmail());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
