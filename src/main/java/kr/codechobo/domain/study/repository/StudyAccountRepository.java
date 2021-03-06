package kr.codechobo.domain.study.repository;

import kr.codechobo.domain.account.entity.Account;
import kr.codechobo.domain.study.entity.Study;
import kr.codechobo.domain.study.entity.StudyAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */

public interface StudyAccountRepository extends JpaRepository<StudyAccount, Long> {
    //todo : queurydsl로 리팩토링 필요.
    Optional<StudyAccount> findStudyAccountByStudyAndAccountAndCanceledJoinIsFalse(Study study, Account account);

    Optional<StudyAccount> findByAccountId(Long accountId);

    List<StudyAccount> findAllByAccount(Account account);
}
