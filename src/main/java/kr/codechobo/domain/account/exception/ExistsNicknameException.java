package kr.codechobo.domain.account.exception;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/16
 */

public class ExistsNicknameException extends RuntimeException {
    public ExistsNicknameException(String nickname) {
        super("이미 존재하는 별명입니다. " + nickname);
    }
}
