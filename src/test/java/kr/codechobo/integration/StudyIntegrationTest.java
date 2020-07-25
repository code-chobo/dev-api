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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/25
 */

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class StudyIntegrationTest {

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
    void setUp() {
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
        EntityExchangeResult<byte[]> entityExchangeResult = client.get()
                .uri("/api/study/1")
                .exchange()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.title").exists()
                .jsonPath("$.description").exists()
                .jsonPath("$.location").exists()
                .jsonPath("$.startDate").exists()
                .jsonPath("$.endDate").exists()

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
