package kr.codechobo.account;

import kr.codechobo.account.exception.ExistsEmailException;
import kr.codechobo.account.exception.ExistsNicknameException;
import kr.codechobo.api.request.JoinRequest;
import kr.codechobo.config.security.TokenManager;
import kr.codechobo.domain.Account;
import kr.codechobo.domain.AccountRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/16
 */

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
        accountService = new AccountService(accountRepository, passwordEncoder,tokenManager);
    }


    @DisplayName("회원가입 잘된다.")
    @Test
    void join() {
        JoinRequest dto = new JoinRequest("email@email.com", "gracelove", "passwordpassword", "passwordpassword");

        accountService.join(dto);

        verify(passwordEncoder).encode(dto.getPassword());
        verify(accountRepository).save(any(Account.class));
    }

    @DisplayName("이미 존재하는 이메일로 가입시도  ExistsEmailException")
    @Test
    void join_email_fail() {
        JoinRequest dto = new JoinRequest("email@email.com", "gracelove", "passwordpassword", "passwordpassword");
        when(accountRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(ExistsEmailException.class, () -> accountService.join(dto));
    }

    @DisplayName("이미 존재하는 닉네임로 가입시도 ExistsNicknameException")
    @Test
    void join_nickname_fail() {
        JoinRequest dto = new JoinRequest("email@email.com", "gracelove", "passwordpassword", "passwordpassword");
        when(accountRepository.existsByNickname(dto.getNickname())).thenReturn(true);

        assertThrows(ExistsNicknameException.class, () -> accountService.join(dto));
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

    @DisplayName("authenticate통과하면 토큰 발급 성공")
    @Test
    void auth_success() {
        Account newAccount = Account.builder()
                .nickname("tester")
                .password("11111111")
                .build();

        given(passwordEncoder.matches("11111111", newAccount.getPassword())).willReturn(true);
        given(tokenManager.createToken(anyString())).willReturn("!@#$%^&*");
        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(newAccount));

        String token = accountService.authenticate("email@email.com", "11111111");

        assertNotNull(token);
    }

    @DisplayName("authenticate 비밀번호 서로 다르면 토큰 발급 실패 - PasswordWrongException.class")
    @Test
    void auth_fail() {
        Account newAccount = Account.builder()
                .nickname("tester")
                .password("22222222")
                .build();

        given(passwordEncoder.matches("11111111", newAccount.getPassword())).willReturn(false);
        given(tokenManager.createToken(anyString())).willReturn("!@#$%^&*");
        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(newAccount));

        assertThrows(PasswordWrongException.class, () -> accountService.authenticate("email@email.com", "11111111"));

    }

}