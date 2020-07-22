package kr.codechobo.study;

import kr.codechobo.domain.Account;
import kr.codechobo.domain.Study;
import kr.codechobo.domain.StudyAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */

public interface StudyAccountRepository extends JpaRepository<StudyAccount, Long> {
    //todo : queurydsl로 리팩토링 필요.
    Optional<StudyAccount> findStudyAccountByStudyAndAccountAndCanceledJoinIsFalse(Study study, Account account);
}
