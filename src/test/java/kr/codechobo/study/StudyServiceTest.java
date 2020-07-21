package kr.codechobo.study;

import kr.codechobo.api.request.CreateStudyRequest;
import kr.codechobo.api.request.JoinStudyRequest;
import kr.codechobo.domain.Account;
import kr.codechobo.domain.AccountRole;
import kr.codechobo.domain.Study;
import kr.codechobo.domain.StudyAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */

@ExtendWith(SpringExtension.class)
class StudyServiceTest {

    @MockBean
    StudyRepository studyRepository;

    @MockBean
    StudyAccountRepository studyAccountRepository;

    StudyService studyService;

    @BeforeEach
    void setUp() {
        studyService = new StudyService(studyRepository, studyAccountRepository);
    }

    @DisplayName("service.createStudy(...) 할 때 studyRepository 에 저장되어야한다.")
    @Test
    void createStudyService() {
        //given
        CreateStudyRequest request = createStudyRequest(1, 2);
        Account account = createAccount();

        //when
        studyService.createStudy(request, account);

        //then
        verify(studyRepository).save(any(Study.class));
    }

    @DisplayName("service.joinStudy(...) 할 때 studyAccountRepository에 저장된다")
    @Test
    void joinStudyService() {
        //given
        given(studyRepository.findById(any())).willReturn(Optional.of(createStudy()));

        //when
        studyService.joinStudy(joinStudyRequest(), createAccount());

        //then
        verify(studyAccountRepository).save(any(StudyAccount.class));
    }

    @DisplayName("service.findStudyById() 하면 study 성공적으로 조회")
    @Test
    void findStudyByIdService() {
        //given
        given(studyRepository.findById(anyLong())).willReturn(Optional.of(createStudy()));

        //when
        Study study = studyService.findStudyById(1L);

        //then
        assertNotNull(study);
        verify(studyRepository).findById(1L);
    }

    private Study createStudy() {
        return Study.createStudy("title", "desc", "서울시 강남구", LocalDateTime.of(2020, 7, 1, 0, 0), LocalDateTime.of(2020, 7, 2, 0, 0), 2, 1, "국민 1111",  createManager());
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

}