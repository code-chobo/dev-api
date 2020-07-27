package kr.codechobo.account.exception;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/27
 */

public class PasswordNotMatchException extends RuntimeException{
    public PasswordNotMatchException() {
        super("패스워드가 일치하지 않습니다.");
    }
}
