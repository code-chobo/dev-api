package kr.codechobo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.codechobo.account.AccountRepository;
import kr.codechobo.api.request.CreateStudyRequest;
import kr.codechobo.api.request.JoinStudyRequest;
import kr.codechobo.config.MockMvcTest;
import kr.codechobo.config.TestProfileConfiguration;
import kr.codechobo.config.WithAccount;
import kr.codechobo.domain.Account;
import kr.codechobo.domain.Location;
import kr.codechobo.domain.Study;
import kr.codechobo.domain.StudyAccount;
import kr.codechobo.study.StudyAccountRepository;
import kr.codechobo.study.StudyRepository;
import kr.codechobo.study.StudyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @DisplayName("스터디 만들기. 성공")
    @WithAccount("grace")
    @Test
    void createStudyTest() throws Exception {
        CreateStudyRequest request = createStudyRequest(0, 10);

        mockMvc.perform(post("/api/study")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studyAccountId").exists());
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
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studyAccountId").exists());
    }

    @DisplayName("스터디 단건 조회")
    @Test
    @WithAccount("tester")
    void findOneStudyTest() throws Exception {
        Account account = accountRepository.findByNickname("tester").get();

        CreateStudyRequest request = createStudyRequest(10, 12);
        Long studyAccountId = studyService.createStudy(request, account);

        StudyAccount studyAccount = studyAccountRepository.findById(studyAccountId).get();
        Long studyId = studyAccount.getStudy().getId();

        mockMvc.perform(get("/api/study/{studyId}", studyId))
                .andDo(print());
    }

    @Test
    @WithAccount("manager")
    void test() throws Exception {
        //given
        Account managerAccount = accountRepository.findByNickname("manager").get();
        CreateStudyRequest request = createStudyRequest(1, 2);
        Long studyAccountManagerId = studyService.createStudy(request, managerAccount);
        StudyAccount studyAccount = studyAccountRepository.findById(studyAccountManagerId).get();

        Account joiner = Account.builder()
                .email("email@email.com")
                .nickname("manager")
                .build();
        accountRepository.save(joiner);

        JoinStudyRequest joinRequest = new JoinStudyRequest(studyAccount.getStudy().getId(), null, null);

        Long joinerStudyAccountId = studyService.joinStudy(joinRequest, joiner);

        //when
        mockMvc.perform(put("/api/study/member/{studyAccountId}", joinerStudyAccountId))
                .andDo(print())
                .andExpect(jsonPath("$.message").value("SUCCESS"));

        //then
        Study study = studyAccount.getStudy();
        assertEquals(2, study.getNumberOfCurrentEnrolment());

    }

    private CreateStudyRequest createStudyRequest(int numberOfMinEnrolment, int numberOfMaxEnrolment) {
        return CreateStudyRequest.builder()
                .title("스프링 스터디")
                .description("스프링을 더 깊게 공부합시다")
                .bankAccount("국민은행 111")
                .leaderContact("010-1111-1111")
                .location(new Location(0,0))
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
