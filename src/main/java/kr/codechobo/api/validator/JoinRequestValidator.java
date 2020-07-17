package kr.codechobo.api.validator;

import kr.codechobo.account.AccountRepository;
import kr.codechobo.account.exception.ExistsEmailException;
import kr.codechobo.account.exception.ExistsNicknameException;
import kr.codechobo.api.request.JoinRequest;
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
public class JoinRequestValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(JoinRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        JoinRequest joinRequest = (JoinRequest) target;
        boolean existsByEmail = accountRepository.existsByEmail(joinRequest.getEmail());
        boolean existsByNickname = accountRepository.existsByNickname(joinRequest.getNickname());

        if(existsByEmail) {
            throw new ExistsEmailException(joinRequest.getEmail());
        }

        if(existsByNickname) {
            throw new ExistsNicknameException(joinRequest.getNickname());
        }
    }
}
