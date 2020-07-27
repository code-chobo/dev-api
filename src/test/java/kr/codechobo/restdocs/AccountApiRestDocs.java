package kr.codechobo.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.codechobo.account.AccountRepository;
import kr.codechobo.account.AccountService;
import kr.codechobo.api.AccountApiController;
import kr.codechobo.api.request.JoinAccountRequest;
import kr.codechobo.api.validator.JoinAccountRequestValidator;
import kr.codechobo.config.MockMvcTest;
import kr.codechobo.config.security.TokenManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/27
 */

@RestDocsTest
@MockMvcTest
public class AccountApiRestDocs {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;


    @Test
    void test() throws Exception {
        JoinAccountRequest request = new JoinAccountRequest("email@email", "tester", "12345678", "12345678");
        mockMvc.perform(post("/api/account")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())

                .andDo(document(
                        "sign-up",
                        requestFields(
                                fieldWithPath("email").description("로그인 할 때 사용할 메일"),
                                fieldWithPath("nickname").description("사이트 내에서 사용할 닉네임"),
                                fieldWithPath("password").description("패스워드"),
                                fieldWithPath("passwordConfirm").description("패스워드 확인")
                        )
                ));
    }
}
