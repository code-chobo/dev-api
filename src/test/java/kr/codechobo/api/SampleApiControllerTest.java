package kr.codechobo.api;

import kr.codechobo.account.AccountRepository;
import kr.codechobo.config.security.TokenManager;
import kr.codechobo.domain.Account;
import kr.codechobo.domain.AccountRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/19
 */

@WebMvcTest(SampleApiController.class)
class SampleApiControllerTest {

    private final static String TOKEN_PREFIX = "Bearer ";
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TokenManager tokenManager;

    @MockBean
    AccountRepository accountRepository;

    @DisplayName("COMMON 유저 테스트")
    @Test
    void commonAccountTest() throws Exception {
        Account account = Account.builder()
                .id(1L)
                .email("email@email.com")
                .nickname("tester")
                .password("12345678")
                .role(AccountRole.COMMON)
                .build();

        String token = "!@#$%^&*";
        given(tokenManager.createToken(account)).willReturn(token);
        given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));

        mockMvc.perform(get("/api/info/common")
                    .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX+token))
                .andDo(print());
    }

    @DisplayName("ADMIN 유저 테스트")
    @Test
    void adminAccountTest() throws Exception {
        Account account = Account.builder()
                .id(1L)
                .email("email@email.com")
                .nickname("tester")
                .password("12345678")
                .role(AccountRole.ADMIN)
                .build();

        String token = "!@#$%^&*";
        given(tokenManager.createToken(account)).willReturn(token);
        given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));

        mockMvc.perform(get("/api/info/admin")
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX+token))
                .andDo(print());
    }

}