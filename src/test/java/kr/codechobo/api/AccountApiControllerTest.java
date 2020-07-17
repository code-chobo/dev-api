package kr.codechobo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.codechobo.account.AccountService;
import kr.codechobo.api.request.JoinRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/15
 */
@ExtendWith(MockitoExtension.class)
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

    @Test
    void join() throws Exception {
        JoinRequest dto = new JoinRequest("email@email.com", "gracelove", "passwordpassword", "passwordpassword");
        mockMvc.perform(post("/api/account")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}