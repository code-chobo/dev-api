package kr.codechobo.account.exception;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/24
 */

public class NotFoundAccountException extends RuntimeException {
    public NotFoundAccountException(Long accountId) {
        super("해당하는 account가 없습니다. : "+ accountId);
    }
}
