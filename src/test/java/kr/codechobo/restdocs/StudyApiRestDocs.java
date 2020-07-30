package kr.codechobo.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.codechobo.account.AccountRepository;
import kr.codechobo.api.request.CreateStudyRequest;
import kr.codechobo.api.request.JoinStudyRequest;
import kr.codechobo.config.MockMvcTest;
import kr.codechobo.config.WithAccount;
import kr.codechobo.config.security.TokenManager;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/27
 */

@RestDocsTest
@MockMvcTest
public class StudyApiRestDocs {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TokenManager tokenManager;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    StudyService studyService;
    @Autowired
    StudyRepository studyRepository;
    @Autowired
    StudyAccountRepository studyAccountRepository;


    @DisplayName("스터디 만들기 api")
    @WithAccount("tester")
    @Test
    void createStudyApi() throws Exception {
        CreateStudyRequest request = createStudyRequest();

        Account account = accountRepository.findByNickname("tester").get();
        String token = tokenManager.createToken(account);

        mockMvc.perform(post("/api/study")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())

                .andDo(document(
                        "create-study",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("로그인 시 발급받는 jwt토큰")
                        ),
                        requestFields(
                                fieldWithPath("title").description("스터디 제목"),
                                fieldWithPath("description").description("스터디 설명"),
                                fieldWithPath("location.x").description("스터디 좌표 x"),
                                fieldWithPath("location.y").description("스터디 좌표 y"),
                                fieldWithPath("startDate").description("스터디 시작 날짜"),
                                fieldWithPath("endDate").description("스터디 끝나는 날짜"),
                                fieldWithPath("numberOfMaxEnrolment").description("최대 모집 인원"),
                                fieldWithPath("numberOfMinEnrolment").description("최소 모집 인원"),
                                fieldWithPath("bankAccount").description("세미나 비용 등을 입금받을 계좌"),
                                fieldWithPath("leaderContact").description("주최자의 연락처")
                        ),
                        responseFields(
                                fieldWithPath("studyAccountId").description("studyAccount의 id")
                        )
                ));
    }



    @DisplayName("스터디 단 건 조회")
    @WithAccount("manager")
    @Test
    void findOneStudy() throws Exception {
        Account account = accountRepository.findByNickname("manager").get();
        Long studyAccountId = studyService.createStudy(createStudyRequest(), account);
        StudyAccount studyAccount = studyAccountRepository.findById(studyAccountId).get();
        Study study = studyAccount.getStudy();
        mockMvc.perform(get("/api/study/{studyId}", study.getId()))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document(
                        "find-study",
                        pathParameters(
                                parameterWithName("studyId").description("study의 id")
                        ),
                        responseFields(
                                fieldWithPath("study").description("루트 경로"),
                                fieldWithPath("study.id").description("study의 id"),
                                fieldWithPath("study.title").description("study의 제목"),
                                fieldWithPath("study.description").description("study의 내용"),
                                fieldWithPath("study.location.x").description("study의 좌표 x"),
                                fieldWithPath("study.location.y").description("study의 좌표 y"),
                                fieldWithPath("study.startDate").description("study 시작 날짜"),
                                fieldWithPath("study.endDate").description("study 끝나는 날짜"),
                                fieldWithPath("study.numberOfMaxEnrolment").description("study 최대 모집 인원"),
                                fieldWithPath("study.numberOfMinEnrolment").description("study 최소 모집 인원"),
                                fieldWithPath("study.numberOfCurrentEnrolment").description("study 현재 인원"),
                                fieldWithPath("study.closed").description("study 모집 마감 플래그"),
                                fieldWithPath("study.bankAccount").description("study 비용 입금 계좌"),
                                fieldWithPath("study.leaderContact").description("study 주최자 연락처"),
                                fieldWithPath("study.createdBy").description("study 주최자의 메일"),
                                fieldWithPath("study.createdDate").description("study 모집 글이 쓰여진 시각"),
                                fieldWithPath("study.modifiedDate").description("study 모집 글이 최종 변경된 시각")
                        )
                ));
    }

    @DisplayName("스터디 참여 취소")
    @WithAccount("joiner")
    @Test
    void cancelJoinStudy() throws Exception {
        Account accountManager = Account.builder()
                .nickname("manager")
                .email("manager@email.com")
                .build();
        accountRepository.save(accountManager);
        Long studyAccountId = studyService.createStudy(createStudyRequest(), accountManager);
        StudyAccount studyAccount = studyAccountRepository.findById(studyAccountId).get();
        Study study = studyAccount.getStudy();

        Account accountMember = accountRepository.findByNickname("joiner").get();
        JoinStudyRequest request = new JoinStudyRequest(study.getId(), "국민 111-111", "010-1234-1234");
        studyService.joinStudy(request, accountMember);

        String token = tokenManager.createToken(accountMember);

        mockMvc.perform(delete("/api/study/{studyId}/member", study.getId())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document(
                        "cancel-join-study",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("login시 발급받은 jwt토큰")
                        ),
                        pathParameters(
                                parameterWithName("studyId").description("참여 취소할 study의 id")
                        ),
                        responseFields(
                                fieldWithPath("studyAccountId").description("취소된 studyAccountId")
                        )
                ));
    }

    @DisplayName("가입한 스터디 전부 조회")
    @Test
    @WithAccount("tester")
    void findAllJoinedStudies() throws Exception {
        Account account = accountRepository.findByNickname("tester").get();
        Study study1 = createStudy("스프링 스터디", "스프링을 더 깊게 알아갑시다.");
        Study study2 = createStudy("JPA 스터디", "JPA를 더 깊게 알아갑시다.");
        StudyAccount studyAccount1 = StudyAccount.builder().study(study1).account(account).build();
        StudyAccount studyAccount2 = StudyAccount.builder().study(study2).account(account).build();
        StudyAccount studyAccount3 = StudyAccount.builder().build();
        studyRepository.save(study1);
        studyRepository.save(study2);
        studyAccountRepository.save(studyAccount1);
        studyAccountRepository.save(studyAccount2);
        studyAccountRepository.save(studyAccount3);

        String token = tokenManager.createToken(account);

        mockMvc.perform(get("/api/study")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studies", hasSize(2)))

                .andDo(document(
                        "find-all-my-studies",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("login시 발급받은 jwt토큰")
                        ),
                        responseFields(
                                fieldWithPath("studies").description("루트"),
                                fieldWithPath("studies[].id").description("study의 id"),
                                fieldWithPath("studies[].title").description("study의 제목"),
                                fieldWithPath("studies[].description").description("study의 내용"),
                                fieldWithPath("studies[].location.x").description("study의 좌표 x"),
                                fieldWithPath("studies[].location.y").description("study의 좌표 y"),
                                fieldWithPath("studies[].startDate").description("study 시작 날짜"),
                                fieldWithPath("studies[].endDate").description("study 끝나는 날짜"),
                                fieldWithPath("studies[].numberOfMaxEnrolment").description("study 최대 모집 인원"),
                                fieldWithPath("studies[].numberOfMinEnrolment").description("study 최소 모집 인원"),
                                fieldWithPath("studies[].numberOfCurrentEnrolment").description("study 현재 인원"),
                                fieldWithPath("studies[].closed").description("study 모집 마감 플래그"),
                                fieldWithPath("studies[].bankAccount").description("study 비용 입금 계좌"),
                                fieldWithPath("studies[].leaderContact").description("study 주최자 연락처"),
                                fieldWithPath("studies[].createdBy").description("study 주최자의 메일"),
                                fieldWithPath("studies[].createdDate").description("study 모집 글이 쓰여진 시각"),
                                fieldWithPath("studies[].modifiedDate").description("study 모집 글이 최종 변경된 시각")
                        )
                ));
    }

    private CreateStudyRequest createStudyRequest() {
        return CreateStudyRequest.builder()
                .title("스프링 스터디")
                .description("스프링을 더 깊게 알아봅시다.")
                .startDate(LocalDateTime.of(2020, 7, 25, 0, 0))
                .endDate(LocalDateTime.of(2020, 7, 30, 0, 0))
                .numberOfMaxEnrolment(10)
                .numberOfMinEnrolment(5)
                .leaderContact("010-1234-1234")
                .bankAccount("국민은행 111-111")
                .location(new Location(0, 0))
                .build();
    }

    private Study createStudy(String title, String description) {
        return Study.builder()
                .title(title)
                .description(description)
                .startDate(LocalDateTime.of(2020, 7, 25, 0, 0))
                .endDate(LocalDateTime.of(2020, 7, 30, 0, 0))
                .numberOfMaxEnrolment(10)
                .numberOfMinEnrolment(5)
                .leaderContact("010-1234-1234")
                .bankAccount("국민은행 111-111")
                .location(new Location(0, 0))
                .build();
    }

}
