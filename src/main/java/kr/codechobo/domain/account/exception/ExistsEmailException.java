package kr.codechobo.domain.account.exception;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/16
 */

public class ExistsEmailException extends RuntimeException {
    public ExistsEmailException(String email) {
        super("이미 존재하는 이메일 입니다. " + email);
    }
}
