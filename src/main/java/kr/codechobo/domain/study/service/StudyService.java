package kr.codechobo.domain.study.service;

import kr.codechobo.domain.study.dto.CreateStudyRequest;
import kr.codechobo.domain.study.dto.JoinStudyRequest;
import kr.codechobo.domain.account.entity.Account;
import kr.codechobo.domain.study.repository.StudyAccountRepository;
import kr.codechobo.domain.study.repository.StudyRepository;
import kr.codechobo.domain.study.entity.Study;
import kr.codechobo.domain.study.entity.StudyAccount;
import kr.codechobo.domain.study.entity.StudyRole;
import kr.codechobo.domain.study.exception.NotFoundStudyAccountException;
import kr.codechobo.domain.study.exception.NotFoundStudyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */


@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyAccountRepository studyAccountRepository;

    public Long createStudy(CreateStudyRequest request, Account managerAccount) {
        Study study = Study.createStudy(request.getTitle(), request.getDescription(), request.getLocation(), request.getStartDate(), request.getEndDate(), request.getNumberOfMaxEnrolment(), request.getNumberOfMinEnrolment(), request.getBankAccount(), request.getLeaderContact());
        Study savedStudy = studyRepository.save(study);

        StudyAccount studyAccount = StudyAccount.CreateStudyAccount(managerAccount, savedStudy, null, StudyRole.MANAGER);
        StudyAccount savedStudyAccount = studyAccountRepository.save(studyAccount);
        savedStudyAccount.acceptJoin();

        return savedStudyAccount.getId();
    }

    public Long joinStudy(JoinStudyRequest request, Account account) {
        Study study = studyRepository.findById(request.getStudyId()).orElseThrow(() -> new NotFoundStudyException(request.getStudyId()));
        StudyAccount studyAccount = StudyAccount.CreateStudyAccount(account, study, request.getRefundBankAccount(), StudyRole.MEMBER);

        StudyAccount savedStudyAccount = studyAccountRepository.save(studyAccount);

        return savedStudyAccount.getId();
    }

    public Long cancelJoin(Long studyId, Account memberAccount) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new NotFoundStudyException(studyId));
        StudyAccount studyAccount = studyAccountRepository.findStudyAccountByStudyAndAccountAndCanceledJoinIsFalse(study, memberAccount).orElseThrow(() -> new NotFoundStudyAccountException(memberAccount.getId()));
        studyAccount.canceledJoin();
        return studyAccount.getId();
    }

    public Study findStudyById(long studyId) {
        return studyRepository.findById(studyId).orElseThrow(() -> new NotFoundStudyException(studyId));
    }


    public void acceptJoin(Account managerAccount, Long studyAccountRoleMemberId) {
        StudyAccount manager = studyAccountRepository.findByAccountId(managerAccount.getId()).orElseThrow(() -> new NotFoundStudyAccountException(studyAccountRoleMemberId));
        if(!manager.isManager()) {
            throw new IllegalStateException("참여를 수락할 권한이 없습니다.");
        }
        StudyAccount studyAccount = studyAccountRepository.findById(studyAccountRoleMemberId).orElseThrow(() -> new NotFoundStudyAccountException(studyAccountRoleMemberId));
        studyAccount.acceptJoin();
    }

    public List<Study> findMyStudies(Account account) {
        List<StudyAccount> studyAccounts = studyAccountRepository.findAllByAccount(account);
        return studyAccounts.stream().map(StudyAccount::getStudy).collect(Collectors.toList());
    }
}
