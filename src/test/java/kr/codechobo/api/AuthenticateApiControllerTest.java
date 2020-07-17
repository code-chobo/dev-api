package kr.codechobo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.codechobo.account.AccountService;
import kr.codechobo.account.exception.PasswordWrongException;
import kr.codechobo.api.request.AuthRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/17
 */

@WebMvcTest(AuthenticateApiController.class)
class AuthenticateApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void authWithValidInput() throws Exception {
        given(accountService.authenticate("email@email.com", "11111111")).willReturn("!@#$%^");

        AuthRequest request = new AuthRequest("email@email.com", "11111111");
        mockMvc.perform(post("/api/authenticate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION));

        verify(accountService).authenticate(eq("email@email.com"), eq("11111111"));
    }

    @Test
    void authWithInvalidInput() throws Exception {
        given(accountService.authenticate("email@email.com", "wrong.pass"))
                .willThrow(PasswordWrongException.class);

        AuthRequest request = new AuthRequest("email@email.com", "wrong.pass");
        mockMvc.perform(post("/api/authenticate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(accountService).authenticate(eq("email@email.com"), eq("wrong.pass"));
    }
}