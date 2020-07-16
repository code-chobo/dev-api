package kr.codechobo.account.dto;

import lombok.Getter;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/15
 */

@Getter
public class LoginRequestDto {
    private String email;
    private String password;

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
