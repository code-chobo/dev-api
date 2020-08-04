package kr.codechobo.domain.study.repository;

import kr.codechobo.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */

public interface StudyRepository extends JpaRepository<Study, Long> {

    Optional<Study> findByTitle(String title);
}
