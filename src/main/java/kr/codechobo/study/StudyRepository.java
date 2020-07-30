package kr.codechobo.study;

import kr.codechobo.domain.Account;
import kr.codechobo.domain.Study;
import kr.codechobo.domain.StudyAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */

public interface StudyRepository extends JpaRepository<Study, Long> {

    Optional<Study> findByTitle(String title);
}
