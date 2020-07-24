package kr.codechobo.study;

import kr.codechobo.api.request.CreateStudyRequest;
import kr.codechobo.api.request.JoinStudyRequest;
import kr.codechobo.domain.Account;
import kr.codechobo.domain.Study;
import kr.codechobo.domain.StudyAccount;
import kr.codechobo.domain.StudyRole;
import kr.codechobo.study.exception.NotFoundStudyAccountException;
import kr.codechobo.study.exception.NotFoundStudyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Long createStudy(CreateStudyRequest request, Account account) {
        Study study = Study.createStudy(request.getTitle(), request.getDescription(), request.getLocation(), request.getStartDate(), request.getEndDate(), request.getNumberOfMaxEnrolment(), request.getNumberOfMinEnrolment(), request.getBankAccount());
        Study savedStudy = studyRepository.save(study);

        StudyAccount studyAccount = StudyAccount.CreateStudyAccount(account, savedStudy, null, null, StudyRole.MANAGER);
        StudyAccount savedStudyAccount = studyAccountRepository.save(studyAccount);

        return savedStudyAccount.getId();
    }

    public Long joinStudy(JoinStudyRequest request, Account account) {
        Study study = studyRepository.findById(request.getStudyId()).orElseThrow(RuntimeException::new);
        StudyAccount studyAccount = StudyAccount.CreateStudyAccount(account, study, request.getRefundBankAccount(), request.getStudentContact(), StudyRole.MEMBER);

        StudyAccount savedStudyAccount = studyAccountRepository.save(studyAccount);
        return savedStudyAccount.getId();
    }

    public Long cancelJoin(Long studyId, Account account) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new NotFoundStudyException(studyId));
        StudyAccount studyAccount = studyAccountRepository.findStudyAccountByStudyAndAccountAndCanceledJoinIsFalse(study, account).orElseThrow(() -> new NotFoundStudyAccountException(account.getId()));
        studyAccount.canceledJoin();
        return studyAccount.getId();
    }

    public Study findStudyById(long studyId) {
        return studyRepository.findById(studyId).orElseThrow(() -> new NotFoundStudyException(studyId));
    }


}
