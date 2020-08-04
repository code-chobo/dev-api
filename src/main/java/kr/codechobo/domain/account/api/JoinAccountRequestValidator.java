package kr.codechobo.domain.account.api;

import kr.codechobo.domain.account.repository.AccountRepository;
import kr.codechobo.domain.account.exception.ExistsEmailException;
import kr.codechobo.domain.account.exception.ExistsNicknameException;
import kr.codechobo.domain.account.exception.PasswordNotMatchException;
import kr.codechobo.domain.account.dto.JoinAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/17
 */

@RequiredArgsConstructor
@Component
public class JoinAccountRequestValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(JoinAccountRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        JoinAccountRequest joinAccountRequest = (JoinAccountRequest) target;
        boolean existsByEmail = accountRepository.existsByEmail(joinAccountRequest.getEmail());
        boolean existsByNickname = accountRepository.existsByNickname(joinAccountRequest.getNickname());

        if(existsByEmail) {
            throw new ExistsEmailException(joinAccountRequest.getEmail());
        }

        if(existsByNickname) {
            throw new ExistsNicknameException(joinAccountRequest.getNickname());
        }

        if(!joinAccountRequest.isPasswordMatchConfirm()) {
            throw new PasswordNotMatchException();
        }
    }
}
