package kr.codechobo.api.exceptionAdvice;

import kr.codechobo.account.PasswordWrongException;
import kr.codechobo.api.result.ApiResult;
import kr.codechobo.api.result.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/17
 */

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(PasswordWrongException.class)
    public ResponseEntity<ApiResult> passwordWrong(PasswordWrongException e) {
        return Result.failure(e.getMessage());
    }
}
