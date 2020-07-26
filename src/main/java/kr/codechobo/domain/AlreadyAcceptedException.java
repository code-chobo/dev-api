package kr.codechobo.domain;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/26
 */

public class AlreadyAcceptedException extends RuntimeException {
    public AlreadyAcceptedException() {
        super("이미 참여를 수락한 사용자입니다.");
    }
}
