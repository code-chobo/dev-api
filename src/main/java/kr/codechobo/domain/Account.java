package kr.codechobo.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/15
 */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    private String password;

    @Enumerated(EnumType.STRING)
    private AccountRole role;

    @Builder
    public Account(String email, String nickname, String password, AccountRole role) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }
}
