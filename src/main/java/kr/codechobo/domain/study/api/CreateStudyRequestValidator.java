package kr.codechobo.domain.study.api;

import kr.codechobo.domain.study.dto.CreateStudyRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/27
 */

@Component
public class CreateStudyRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CreateStudyRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateStudyRequest createStudyRequest = (CreateStudyRequest) target;
        LocalDateTime startDate = createStudyRequest.getStartDate();
        LocalDateTime endDate = createStudyRequest.getEndDate();

        if(startDate.isAfter(endDate)) {
            throw new ValidationException("스터디의 시작 날짜는 끝나는 날짜 이전이어야 합니다.");
        }
    }
}
