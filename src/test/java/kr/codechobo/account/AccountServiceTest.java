package kr.codechobo.account;

import kr.codechobo.account.dto.JoinRequestDto;
import kr.codechobo.account.exception.ExistsEmailException;
import kr.codechobo.account.exception.ExistsNicknameException;
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
import static org.mockito.ArgumentMatchers.any;
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

    AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountRepository, passwordEncoder);
    }


    @DisplayName("회원가입 잘된다.")
    @Test
    void join() {
        JoinRequestDto dto = new JoinRequestDto("email@email.com", "gracelove", "passwordpassword", "passwordpassword");

        accountService.join(dto);

        verify(passwordEncoder).encode(dto.getPassword());
        verify(accountRepository).save(any(Account.class));
    }

    @DisplayName("이미 존재하는 이메일로 등록시 ExistsEmailException")
    @Test
    void join_email_fail() {
        JoinRequestDto dto = new JoinRequestDto("email@email.com", "gracelove", "passwordpassword", "passwordpassword");
        when(accountRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(ExistsEmailException.class, () -> accountService.join(dto));
    }

    @DisplayName("이미 존재하는 닉네임로 등록시 ExistsNicknameException")
    @Test
    void join_nickname_fail() {
        JoinRequestDto dto = new JoinRequestDto("email@email.com", "gracelove", "passwordpassword", "passwordpassword");
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

}