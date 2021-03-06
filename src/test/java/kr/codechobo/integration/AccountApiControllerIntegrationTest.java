package kr.codechobo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.codechobo.domain.account.dto.JoinAccountRequest;
import kr.codechobo.infra.MockMvcTest;
import kr.codechobo.infra.TestProfileConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/26
 */

@Import(TestProfileConfiguration.class)
@MockMvcTest
public class AccountApiControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("회원가입 성공")
    @Test
    void joinAccount() throws Exception {
        JoinAccountRequest dto = new JoinAccountRequest("email@email.com", "gracelove", "passwordpassword", "passwordpassword");
        mockMvc.perform(post("/api/account")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").exists());
    }
}
