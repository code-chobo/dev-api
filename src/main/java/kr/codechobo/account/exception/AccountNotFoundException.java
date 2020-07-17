package kr.codechobo.account.exception;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/16
 */

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) {
        super("해당 사용자가 없습니다. " + id);
    }

    public AccountNotFoundException(String emailOrNickname) {
        super("해당 사용자가 없습니다. " + emailOrNickname);
    }
}
