package kr.codechobo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.codechobo.account.AccountRepository;
import kr.codechobo.api.request.CreateStudyRequest;
import kr.codechobo.api.request.JoinStudyRequest;
import kr.codechobo.config.security.TokenManager;
import kr.codechobo.domain.Account;
import kr.codechobo.domain.AccountRole;
import kr.codechobo.domain.Study;
import kr.codechobo.study.StudyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */

@WebMvcTest(StudyApiController.class)
class StudyApiControllerTest {

    private static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TokenManager tokenManager;

    @MockBean
    AccountRepository accountRepository;

    @MockBean
    StudyService studyService;


    @DisplayName("startDate가 EndDate 보다 늦으면 안된다. 4xx client error")
    @Test
    void isStartDateBeforeThanEndDate() throws Exception {
        //given
        Account account = createAccount();

        String token = "!@#$%^&*";
        given(tokenManager.createToken(account)).willReturn(token);
        given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));

        CreateStudyRequest request = CreateStudyRequest.builder()
                .startDate(LocalDateTime.of(2020, Month.DECEMBER, 1, 0, 0))
                .endDate(LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0))
                .build();

        //when
        mockMvc.perform(post("/api/study")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        //then
     }

    @DisplayName("스터디 만들기 api - 정상값 입력시 201.")
    @Test
    void validArguments() throws Exception {
        //given
        Account account = createAccount();

        String token = "!@#$%^&*";
        given(tokenManager.createToken(account)).willReturn(token);
        given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));

        CreateStudyRequest request = createStudyRequest(1, 1);

        //when
        mockMvc.perform(post("/api/study")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token))
                .andDo(print())
                .andExpect(status().isCreated());
        //then
    }

    @DisplayName("스터디 가입 잘 된다.")
    @Test
    void joinStudy() throws Exception{
        Account account = createAccount();

        String token = "!@#$%^&*";
        given(tokenManager.createToken(account)).willReturn(token);
        given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));

        mockMvc.perform(post("/api/study/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(joinStudyRequest()))
                    .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("스터디 단 건 조회")
    @Test
    void findStudyById() throws Exception{
        given(studyService.findStudyById(anyLong())).willReturn(createStudy());
        mockMvc.perform(get("/api/study/{studyId}", 1L))
                .andDo(print());

    }

    private CreateStudyRequest createStudyRequest(int numberOfMinEnrolment, int numberOfMaxEnrolment) {
        return CreateStudyRequest.builder()
                .numberOfMinEnrolment(numberOfMinEnrolment)
                .numberOfMaxEnrolment(numberOfMaxEnrolment)
                .startDate(LocalDateTime.of(2020, Month.DECEMBER, 1, 0, 0))
                .endDate(LocalDateTime.of(2020, Month.DECEMBER, 12, 0, 0))
                .build();
    }

    private JoinStudyRequest joinStudyRequest() {
        return new JoinStudyRequest(0L, "국민은행 1111-1111", "010-1234-1234");
    }

    private Account createAccount() {
        return Account.builder()
                .id(1L)
                .email("email@email.com")
                .nickname("tester")
                .password("12345678")
                .role(AccountRole.COMMON)
                .build();
    }

    private Account createManager() {
        return Account.builder()
                .id(2L)
                .email("email@email.com")
                .nickname("Manager")
                .password("12345678")
                .role(AccountRole.COMMON)
                .build();
    }

    private Study createStudy() {
        return Study.builder()
                .id(1L)
                .title("title")
                .description("desc")
                .location("강남구")
                .startDate(LocalDateTime.of(2020, Month.DECEMBER, 1, 0, 0))
                .endDate(LocalDateTime.of(2020, Month.DECEMBER, 10, 0, 0))
                .numberOfMaxEnrolment(2)
                .numberOfMinEnrolment(1)
                .bankAccount("국민 111")
                .build();
    }



}