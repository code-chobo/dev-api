package kr.codechobo.study;

import kr.codechobo.account.AccountRepository;
import kr.codechobo.domain.Account;
import kr.codechobo.domain.Study;
import kr.codechobo.domain.StudyAccount;
import kr.codechobo.domain.StudyRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/22
 */

@DataJpaTest
class StudyAccountRepositoryTest {

    @Autowired
    StudyAccountRepository studyAccountRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    AccountRepository accountRepository;

    @DisplayName("study, account -> studyAccount.canceledJoin 이 false인 데이터 조회한다.")
    @Test
    void findStudyAccountByStudyAndAccountAndCanceledJoinIsFalse() {
        //given
        Account account = Account.builder().build();
        Study study = Study.builder().build();
        StudyAccount studyAccount = StudyAccount.CreateStudyAccount(account, study, "국민 111", "010-1111-1111", StudyRole.MEMBER);

        accountRepository.save(account);
        studyRepository.save(study);
        studyAccountRepository.save(studyAccount);

        //when
        Optional<StudyAccount> findStudyAccount = studyAccountRepository.findStudyAccountByStudyAndAccountAndCanceledJoinIsFalse(study, account);

        //then
        assertTrue(findStudyAccount.isPresent());
    }

    @DisplayName("study / account / studyAccount.canceledJoin 이 True면 데이터 empty.")
    @Test
    void findStudyAccountByStudyAndAccountAndCanceledJoinIsTrue() {
        //given
        Account account = Account.builder().build();
        Study study = Study.builder().build();
        StudyAccount studyAccount = StudyAccount.CreateStudyAccount(account, study, "국민 111", "010-1111-1111", StudyRole.MEMBER);

        studyAccount.canceledJoin();

        accountRepository.save(account);
        studyRepository.save(study);
        studyAccountRepository.save(studyAccount);

        //when
        Optional<StudyAccount> findStudyAccount = studyAccountRepository.findStudyAccountByStudyAndAccountAndCanceledJoinIsFalse(study, account);

        //then
        assertTrue(findStudyAccount.isEmpty());
    }
}