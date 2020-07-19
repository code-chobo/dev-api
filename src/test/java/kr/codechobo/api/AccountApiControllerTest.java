package kr.codechobo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.codechobo.account.AccountRepository;
import kr.codechobo.account.AccountService;
import kr.codechobo.account.exception.ExistsEmailException;
import kr.codechobo.api.request.JoinRequest;
import kr.codechobo.api.validator.JoinRequestValidator;
import kr.codechobo.config.security.TokenManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
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
    JoinRequestValidator joinRequestValidator;

    @MockBean
    AccountRepository accountRepository;

    @MockBean
    TokenManager tokenManager;

    @DisplayName("가입 성공")
    @Test
    void join() throws Exception {
        JoinRequest dto = new JoinRequest("email@email.com", "gracelove", "passwordpassword", "passwordpassword");
        when(joinRequestValidator.supports(any())).thenReturn(true);
        mockMvc.perform(post("/api/account")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("이미 존재하는 이메일로 가입시도  ExistsEmailException")
    @Test
    void join_email_fail() throws Exception {
        JoinRequest dto = new JoinRequest("email@email.com", "gracelove", "passwordpassword", "passwordpassword");
        when(joinRequestValidator.supports(any())).thenReturn(true);
        doThrow(ExistsEmailException.class).when(joinRequestValidator).validate(any(), any());

        mockMvc.perform(post("/api/account")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("이미 존재하는 닉네임로 가입시도 ExistsNicknameException")
    @Test
    void join_nickname_fail() throws Exception {
        JoinRequest dto = new JoinRequest("email@email.com", "gracelove", "passwordpassword", "passwordpassword");
        when(joinRequestValidator.supports(any())).thenReturn(true);
        doThrow(ExistsEmailException.class).when(joinRequestValidator).validate(any(), any());

        mockMvc.perform(post("/api/account")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}