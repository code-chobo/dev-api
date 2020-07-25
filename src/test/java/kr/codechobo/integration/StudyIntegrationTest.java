package kr.codechobo.integration;

import kr.codechobo.account.AccountRepository;
import kr.codechobo.account.AccountService;
import kr.codechobo.api.request.CreateStudyRequest;
import kr.codechobo.api.request.JoinAccountRequest;
import kr.codechobo.config.security.TokenManager;
import kr.codechobo.study.StudyAccountRepository;
import kr.codechobo.study.StudyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.WebApplicationContext;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;


/**
 * @author : Eunmo Hong
 * @since : 2020/07/25
 */

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class StudyIntegrationTest {

    @RegisterExtension
    RestDocumentationExtension restDocumentation = new RestDocumentationExtension();

    @Autowired
    WebTestClient client;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    StudyAccountRepository studyAccountRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    TokenManager tokenManager;

    String token;


    @BeforeEach
    public void setUp(ApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        this.client = WebTestClient.bindToApplicationContext(context)
                .configureClient()
                .filter(documentationConfiguration(restDocumentation))
                .build();

        JoinAccountRequest joinRequest = new JoinAccountRequest("email@email.com", "tester", "123456", "123456");
        Long join = accountService.join(joinRequest);
        token = tokenManager.createToken(accountRepository.findById(join).get());

        CreateStudyRequest createStudyRequest = getCreateStudyRequest();

        client.post()
                .uri("/api/study")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(Mono.just(createStudyRequest), CreateStudyRequest.class)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @AfterEach
    void tearDown() {
        studyAccountRepository.deleteAll();
        studyRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @DisplayName("스터디 만들기 - 성공")
    @Test
    void createStudy() {
        CreateStudyRequest createStudyRequest = getCreateStudyRequest();

        client.post()
                .uri("/api/study")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(Mono.just(createStudyRequest), CreateStudyRequest.class)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @DisplayName("스터디 단건 조회")
    @Test
    void getStudy() {
        client.get()
                .uri("/api/study/1")
                .exchange()
                .expectBody()
                .consumeWith(document("find-study",
                        responseFields(
                                fieldWithPath("$.id").description("study의 id"),
                                fieldWithPath("$.title").description("study의 제목")
                        )
                ));

    }

    private CreateStudyRequest getCreateStudyRequest() {
        return CreateStudyRequest.builder()
                .title("스프링 스터디")
                .description("스프링을 더 깊게 알아갑시다")
                .startDate(LocalDateTime.of(2020, 7, 29, 0, 0))
                .endDate(LocalDateTime.of(2020, 8, 1, 0, 0))
                .numberOfMaxEnrolment(10)
                .numberOfMinEnrolment(0)
                .leaderContact("010-1234-1234")
                .bankAccount("국민 101")
                .location("서울시 강남구")
                .build();
    }
}
