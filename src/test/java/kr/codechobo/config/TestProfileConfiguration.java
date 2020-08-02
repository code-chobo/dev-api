package kr.codechobo.config;

import kr.codechobo.domain.Account;
import kr.codechobo.domain.AccountRole;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/26
 */

@TestConfiguration
@Profile("test")
@EnableJpaAuditing(auditorAwareRef = "testAuditorProvider")
public class TestProfileConfiguration {

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        return configurer -> configurer.operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint());
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setDefaultCharset(StandardCharsets.UTF_8);
        return jsonConverter;
    }

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
