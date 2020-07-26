package kr.codechobo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.codechobo.account.AccountRepository;
import kr.codechobo.api.request.CreateStudyRequest;
import kr.codechobo.api.request.JoinStudyRequest;
import kr.codechobo.config.MockMvcTest;
import kr.codechobo.config.TestProfileConfiguration;
import kr.codechobo.config.WithAccount;
import kr.codechobo.domain.Account;
import kr.codechobo.domain.StudyAccount;
import kr.codechobo.study.StudyAccountRepository;
import kr.codechobo.study.StudyRepository;
import kr.codechobo.study.StudyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/26
 */

@Import(TestProfileConfiguration.class)
@MockMvcTest
public class StudyApiControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    StudyService studyService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    StudyAccountRepository studyAccountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @DisplayName("스터디 만들기. 성공")
    @WithAccount("grace")
    @Test
    void createStudyTest() throws Exception {
        CreateStudyRequest request = createStudyRequest(0, 10);
        Account account = accountRepository.findByNickname("grace").get();

        Long studyAccountId = studyService.createStudy(request, account);

        StudyAccount studyAccount = studyAccountRepository.findById(studyAccountId).get();
        Long studyId = studyAccount.getStudy().getId();

        mockMvc.perform(get("/api/study/{studyId}", studyId))
                .andDo(print());
    }

    @DisplayName("스터디 참가 성공")
    @WithAccount("joiner")
    @Test
    void joinStudyTest() throws Exception {
        Account account = accountRepository.findByNickname("joiner").get();

        CreateStudyRequest request = createStudyRequest(10, 12);
        Long studyAccountId = studyService.createStudy(request, account);

        StudyAccount studyAccount = studyAccountRepository.findById(studyAccountId).get();
        Long studyId = studyAccount.getStudy().getId();

        mockMvc.perform(post("/api/study/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(joinStudyRequest(studyId))))
                .andDo(print());
    }

    private CreateStudyRequest createStudyRequest(int numberOfMinEnrolment, int numberOfMaxEnrolment) {
        return CreateStudyRequest.builder()
                .title("스프링 스터디")
                .description("스프링을 더 깊게 공부합시다")
                .bankAccount("국민은행 111")
                .leaderContact("010-1111-1111")
                .location("서울시 강남구")
                .numberOfMinEnrolment(numberOfMinEnrolment)
                .numberOfMaxEnrolment(numberOfMaxEnrolment)
                .startDate(LocalDateTime.of(2020, Month.DECEMBER, 1, 0, 0))
                .endDate(LocalDateTime.of(2020, Month.DECEMBER, 12, 0, 0))
                .build();
    }

    private JoinStudyRequest joinStudyRequest(Long studyId) {
        return new JoinStudyRequest(studyId, "국민은행 1111-1111", "010-1234-1234");
    }
}
