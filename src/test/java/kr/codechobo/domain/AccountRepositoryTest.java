package kr.codechobo.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/15
 */

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    private static final String EMAIL = "email@email.com";
    private static final String NICKNAME = "GraceLove";
    private static final String PASSWORD = "12345678";


    @BeforeEach
    void setUp() {
        Account account = Account.builder()
                .email(EMAIL)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();

        accountRepository.save(account);
    }

    @Test
    @DisplayName("findByEmail 정상 작동")
    void findByEmail() {
        Account account = accountRepository.findByEmail(EMAIL).orElseThrow(RuntimeException::new);
        assertEquals(EMAIL, account.getEmail());
        assertEquals(NICKNAME, account.getNickname());
        assertEquals(PASSWORD, account.getPassword());
    }

    @Test
    @DisplayName("findByNickname 정상 작동")
    void findByNickname() {
        Account account = accountRepository.findByNickname(NICKNAME).orElseThrow(RuntimeException::new);
        assertEquals(EMAIL, account.getEmail());
        assertEquals(NICKNAME, account.getNickname());
        assertEquals(PASSWORD, account.getPassword());
    }

}