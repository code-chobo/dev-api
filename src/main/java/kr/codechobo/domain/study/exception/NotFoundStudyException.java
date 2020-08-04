package kr.codechobo.domain.study.exception;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/22
 */

public class NotFoundStudyException extends RuntimeException {
    public NotFoundStudyException(long studyId) {
        super("해당하는 스터디를 찾지 못했습니다. : " + studyId);
    }
}
