package kr.codechobo.domain.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/15
 */

@NoArgsConstructor
@Getter
public class AuthRequest {
    private String email;
    private String password;

    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
