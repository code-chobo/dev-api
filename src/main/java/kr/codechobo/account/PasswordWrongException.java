package kr.codechobo.account;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/17
 */

public class PasswordWrongException extends RuntimeException {
    public PasswordWrongException() {
        super("비밀번호가 맞지 않습니다.");
    }
}
