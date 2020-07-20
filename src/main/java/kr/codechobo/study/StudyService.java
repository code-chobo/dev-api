package kr.codechobo.study;

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

    public Long createStudy() {
        return null;
    }
}
