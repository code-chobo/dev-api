package kr.codechobo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.codechobo.account.AccountRepository;
import kr.codechobo.api.request.StudyCreateRequest;
import kr.codechobo.config.security.TokenManager;
import kr.codechobo.domain.Account;
import kr.codechobo.domain.AccountRole;
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


    @DisplayName("startDate가 EndDate 보다 늦으면 안된다.")
    @Test
    void isStartDateBeforeThanEndDate() throws Exception {
        //given
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

        StudyCreateRequest request = StudyCreateRequest.builder()
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

    @DisplayName("최소인원이 최대인원보다 많으면 안된다.")
    @Test
    void isMinEnrolmentLessThanMaxEnrolment() throws Exception {
        //given
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

        StudyCreateRequest request = StudyCreateRequest.builder()
                .numberOfMinEnrolment(2)
                .numberOfMaxEnrolment(1)
                .startDate(LocalDateTime.of(2020, Month.DECEMBER, 1, 0, 0))
                .endDate(LocalDateTime.of(2020, Month.DECEMBER, 12, 0, 0))
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

    @DisplayName("정상값 입력시 200.")
    @Test
    void validArguments() throws Exception {
        //given
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

        StudyCreateRequest request = StudyCreateRequest.builder()
                .numberOfMinEnrolment(1)
                .numberOfMaxEnrolment(1)
                .startDate(LocalDateTime.of(2020, Month.DECEMBER, 1, 0, 0))
                .endDate(LocalDateTime.of(2020, Month.DECEMBER, 12, 0, 0))
                .build();

        //when
        mockMvc.perform(post("/api/study")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk());
        //then
    }



}