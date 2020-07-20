package kr.codechobo.study;

import kr.codechobo.domain.StudyAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */

public interface StudyAccountRepository extends JpaRepository<StudyAccount, Long> {
}
