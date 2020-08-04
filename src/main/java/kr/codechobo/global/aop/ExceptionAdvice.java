package kr.codechobo.global.aop;

import kr.codechobo.domain.account.exception.ExistsEmailException;
import kr.codechobo.domain.account.exception.ExistsNicknameException;
import kr.codechobo.domain.account.exception.PasswordWrongException;
import kr.codechobo.global.resultspec.ApiResult;
import kr.codechobo.global.resultspec.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

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

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResult> validationException(ValidationException e) {
        return Result.failure(e.getMessage());
    }
}
