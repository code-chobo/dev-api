package kr.codechobo.account.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/15
 */

@Getter
public class JoinRequestDto {

    @Email
    private String email;

    @Length(min = 4, max = 30)
    private String nickname;

    @Length(min = 6, max = 50)
    private String password;

    @Length(min = 6, max = 50)
    private String passwordConfirm;

    public JoinRequestDto(String email, String nickname, String password, String passwordConfirm) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    @AssertTrue
    public boolean isPasswordMatchConfirm() {
        return password.equals(passwordConfirm);
    }


}
