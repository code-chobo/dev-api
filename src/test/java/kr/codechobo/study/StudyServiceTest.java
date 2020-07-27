package kr.codechobo.study;

import kr.codechobo.api.request.CreateStudyRequest;
import kr.codechobo.api.request.JoinStudyRequest;
import kr.codechobo.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */

@ActiveProfiles("test")
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

    @DisplayName("service.createStudy(...) 할 때 studyRepository 와 StudyAccountRepository 에 저장되어야한다.")
    @Test
    void createStudyService() {
        //given
        CreateStudyRequest request = createStudyRequest(1, 2);
        Account account = createAccountManager();
        given(studyRepository.save(any(Study.class))).willReturn(createStudy(2,1));
        given(studyAccountRepository.save(any(StudyAccount.class))).willReturn(createStudyAccountRoleManager());

        //when
        studyService.createStudy(request, account);

        //then
        verify(studyRepository).save(any(Study.class));
        verify(studyAccountRepository).save(any(StudyAccount.class));
    }

    @DisplayName("service.createStudy(...) 할 때 Study의 numberOfCurrentEnrolment 증가.")
    @Test
    void createStudyService_with_numberOfCurrentEnrolment_increase() {
        //given
        Study study = createStudy(2,1);
        given(studyRepository.save(any(Study.class))).willReturn(study);
        StudyAccount studyAccountRoleManager = createStudyAccountRoleManager();
        given(studyAccountRepository.save(any(StudyAccount.class))).willReturn(studyAccountRoleManager);

        //when
        studyService.createStudy(createStudyRequest(1, 2), createAccountManager());

        //then
        assertEquals(1, studyAccountRoleManager.getStudy().getNumberOfCurrentEnrolment());
    }

    @DisplayName("service.joinStudy(...) 할 때 studyAccountRepository에 저장된다")
    @Test
    void joinStudyService() {
        //given
        given(studyRepository.findById(any())).willReturn(Optional.of(createStudy(2,1)));
        given(studyAccountRepository.save(any(StudyAccount.class))).willReturn(createStudyAccountRoleMember());
        //when
        studyService.joinStudy(joinStudyRequest(), createAccountMember());

        //then
        verify(studyAccountRepository).save(any(StudyAccount.class));
    }



    @DisplayName("service.findStudyById() 하면 study 성공적으로 조회")
    @Test
    void findStudyByIdService() {
        //given
        given(studyRepository.findById(anyLong())).willReturn(Optional.of(createStudy(2,1)));

        //when
        Study study = studyService.findStudyById(1L);

        //then
        assertNotNull(study);
        verify(studyRepository).findById(1L);
    }

    private StudyAccount createStudyAccountRoleMember() {
        return StudyAccount.builder()
                .study(createStudy(2, 1))
                .account(createAccountMember())
                .id(1L)
                .build();
    }

    private StudyAccount createStudyAccountRoleManager() {
        return StudyAccount.builder()
                .study(createStudy(2, 1))
                .account(createAccountManager())
                .id(2L)
                .build();
    }

    private Study createStudy(int maxEnrolment, int minEnrolment) {
        return Study.createStudy("title", "desc", new Location(0, 0), LocalDateTime.of(2020, 7, 1, 0, 0), LocalDateTime.of(2020, 7, 2, 0, 0), maxEnrolment, minEnrolment, "국민 1111", "010-1234-1234");
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

    private Account createAccountManager() {
        return Account.builder()
                .id(2L)
                .email("email@email.com")
                .nickname("Manager")
                .password("12345678")
                .role(AccountRole.COMMON)
                .build();
    }

    private Account createAccountMember() {
        return Account.builder()
                .id(1L)
                .email("email@email.com")
                .nickname("tester")
                .password("12345678")
                .role(AccountRole.COMMON)
                .build();
    }

}