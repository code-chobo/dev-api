package kr.codechobo.domain.account.dto;

import kr.codechobo.domain.account.dto.JoinAccountRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/15
 * <p>
 * see {@link JoinAccountRequest}
 */

@ActiveProfiles("test")
class JoinAccountRequestTest {

    Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("email이 email 형식이 아닐 때 검증 실패해야한다.")
    @Test
    void shouldFailEmailWrongFormat() {
        JoinAccountRequest dto = new JoinAccountRequest("email", "nickname", "11111111", "11111111");
        Set<ConstraintViolation<JoinAccountRequest>> validate = validator.validate(dto);
        assertEquals(1, validate.size());
    }

    @DisplayName("nickname 길이가 3일 때 검증 실패해야한다.")
    @Test
    void shouldFailNicknameWrongFormat() {
        JoinAccountRequest dto = new JoinAccountRequest("email@email.com", "abc", "11111111", "11111111");
        Set<ConstraintViolation<JoinAccountRequest>> validate = validator.validate(dto);
        assertEquals(1, validate.size());
    }

    @DisplayName("password 길이가 5일 때 검증 실패해야한다.")
    @Test
    void shouldFailPasswordWrongFormat() {
        JoinAccountRequest dto = new JoinAccountRequest("email@email.com", "nic", "1111111", "1111111");
        Set<ConstraintViolation<JoinAccountRequest>> validate = validator.validate(dto);
        assertEquals(1, validate.size());
    }

    @DisplayName("password와 passwordConfirm이 서로 다를 때 검증 실패해야한다.")
    @Test
    void shouldFailPasswordNotMatchConfirm() {
        JoinAccountRequest dto = new JoinAccountRequest("email@email.com", "nickname", "111111", "222222");
        Set<ConstraintViolation<JoinAccountRequest>> validate = validator.validate(dto);
        assertEquals(1, validate.size());
    }

    @DisplayName("모든 형식이 맞을 때 검증 성공해야한다.")
    @Test
    void shouldSuccessFieldsAllCorrectValue() {
        JoinAccountRequest dto = new JoinAccountRequest("email@email.com", "nickname", "111111", "111111");
        Set<ConstraintViolation<JoinAccountRequest>> validate = validator.validate(dto);
        assertEquals(0, validate.size());
    }
}