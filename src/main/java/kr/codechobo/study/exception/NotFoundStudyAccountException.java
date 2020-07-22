package kr.codechobo.study.exception;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/22
 */

public class NotFoundStudyAccountException extends RuntimeException {
    public NotFoundStudyAccountException(Long accountId) {
        super("스터디에 참여중이 아닙니다 accountId : " + accountId);
    }
}
