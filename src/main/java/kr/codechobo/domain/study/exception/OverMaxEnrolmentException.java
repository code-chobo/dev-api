package kr.codechobo.domain.study.exception;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/26
 */

public class OverMaxEnrolmentException extends RuntimeException {
    public OverMaxEnrolmentException(int numberOfMaxEnrolment, int numberOfCurrentEnrolment) {
        super("참여 인원이 꽉 찼습니다. 현재인원 : " + numberOfCurrentEnrolment + " 최대인원 : " + numberOfMaxEnrolment);
    }
}
