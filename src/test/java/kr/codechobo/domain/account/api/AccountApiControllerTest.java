package kr.codechobo.domain.account.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.codechobo.domain.account.api.AccountApiController;
import kr.codechobo.domain.account.repository.AccountRepository;
import kr.codechobo.domain.account.service.AccountService;
import kr.codechobo.domain.account.exception.ExistsEmailException;
import kr.codechobo.domain.account.dto.JoinAccountRequest;
import kr.codechobo.domain.account.api.JoinAccountRequestValidator;
import kr.codechobo.global.util.TokenManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/15
 */
@ActiveProfiles("test")
@WebMvcTest(AccountApiController.class)
class AccountApiControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountApiController accountApiController;

    @MockBean
    AccountService accountService;

    @MockBean
    JoinAccountRequestValidator joinAccountRequestValidator;

    @MockBean
    AccountRepository accountRepository;

    @MockBean
    TokenManager tokenManager;

    @MockBean
    AuthenticationManager authenticationManager;

    @DisplayName("가입 성공")
    @Test
    void join() throws Exception {
        JoinAccountRequest dto = new JoinAccountRequest("email@email.com", "gracelove", "passwordpassword", "passwordpassword");
        when(joinAccountRequestValidator.supports(any())).thenReturn(true);
        mockMvc.perform(post("/api/account")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("이미 존재하는 이메일로 가입시도  ExistsEmailException")
    @Test
    void join_email_fail() throws Exception {
        JoinAccountRequest dto = new JoinAccountRequest("email@email.com", "gracelove", "passwordpassword", "passwordpassword");
        when(joinAccountRequestValidator.supports(any())).thenReturn(true);
        doThrow(ExistsEmailException.class).when(joinAccountRequestValidator).validate(any(), any());

        mockMvc.perform(post("/api/account")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("이미 존재하는 닉네임로 가입시도 ExistsNicknameException")
    @Test
    void join_nickname_fail() throws Exception {
        JoinAccountRequest dto = new JoinAccountRequest("email@email.com", "gracelove", "passwordpassword", "passwordpassword");
        when(joinAccountRequestValidator.supports(any())).thenReturn(true);
        doThrow(ExistsEmailException.class).when(joinAccountRequestValidator).validate(any(), any());

        mockMvc.perform(post("/api/account")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}