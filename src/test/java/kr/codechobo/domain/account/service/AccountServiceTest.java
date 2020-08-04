package kr.codechobo.domain.account.service;

import kr.codechobo.domain.account.dto.JoinAccountRequest;
import kr.codechobo.global.util.TokenManager;
import kr.codechobo.domain.account.repository.AccountRepository;
import kr.codechobo.domain.account.service.AccountService;
import kr.codechobo.domain.account.entity.Account;
import kr.codechobo.domain.account.entity.AccountRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/16
 */

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class AccountServiceTest {

    @MockBean
    AccountRepository accountRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    TokenManager tokenManager;

    AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountRepository, passwordEncoder);
    }


    @DisplayName("회원가입 잘된다.")
    @Test
    void join() {
        JoinAccountRequest dto = new JoinAccountRequest("email@email.com", "gracelove", "passwordpassword", "passwordpassword");

        accountService.join(dto);

        verify(passwordEncoder).encode(dto.getPassword());
        verify(accountRepository).save(any(Account.class));
    }

    @DisplayName("findById 잘된다.")
    @Test
    void findById() {

        Account newAccount = Account.builder()
                .email("email@email.com")
                .nickname("gracelove")
                .password("12345678")
                .role(AccountRole.COMMON)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(newAccount));

        Account findAccount = accountService.findAccountById(1L);

        assertNotNull(findAccount);
    }

}