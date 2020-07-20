package kr.codechobo.api.exceptionAdvice;

import kr.codechobo.account.exception.PasswordWrongException;
import kr.codechobo.account.exception.ExistsEmailException;
import kr.codechobo.account.exception.ExistsNicknameException;
import kr.codechobo.api.result.ApiResult;
import kr.codechobo.api.result.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(ExistsNicknameException.class)
    public ResponseEntity<ApiResult> existsNickname(ExistsNicknameException e) {
        return Result.failure(e.getMessage());
    }

    @ExceptionHandler(ExistsEmailException.class)
    public ResponseEntity<ApiResult> existsEmail(ExistsEmailException e) {
        return Result.failure(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult> methodArgsValid(MethodArgumentNotValidException e) {
        return Result.failure(e.getMessage());
    }
}
