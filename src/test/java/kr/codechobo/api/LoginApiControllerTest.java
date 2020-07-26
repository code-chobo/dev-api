package kr.codechobo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.codechobo.account.AccountService;
import kr.codechobo.api.request.AuthRequest;
import kr.codechobo.api.request.JoinAccountRequest;
import kr.codechobo.config.MockMvcTest;
import kr.codechobo.config.TestProfileConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/19
 */
@Import(TestProfileConfiguration.class)
@MockMvcTest
public class LoginApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("/login 올바른 이메일, 패스워드 / 토큰 발급 성공")
    @Test
    void auth_success() throws Exception{

        String email = "email@email.com";
        String password = "111111";
        JoinAccountRequest joinAccountRequest = new JoinAccountRequest(email,"gracelove",password,password);
        accountService.join(joinAccountRequest);

        AuthRequest authRequest = new AuthRequest(email, password);

        mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION));

    }

    @DisplayName("/login 잘못된 이메일, 패스워드 / 토큰 발급 실패")
    @Test
    void auth_fail() throws Exception {
        String email = "email@email.com";
        String password = "111111";
        JoinAccountRequest joinAccountRequest = new JoinAccountRequest(email,"gracelove",password,password);
        accountService.join(joinAccountRequest);

        AuthRequest authRequest = new AuthRequest(email, "wrong.password");

        mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION));
    }
}
