package kr.codechobo.api.request;

import lombok.Getter;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/15
 */

@Getter
public class AuthRequest {
    private String email;
    private String password;

    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
