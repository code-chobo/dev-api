package kr.codechobo.domain;

import lombok.*;

import javax.persistence.*;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/15
 */

@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = {"id", "email", "nickname"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Account extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    private String email;

    private String nickname;

    private String password;

    @Enumerated(EnumType.STRING)
    private AccountRole role;

    private String contact;

    @Builder
    public Account(Long id, String email, String nickname, String password, AccountRole role, String contact) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.contact = contact;
    }
}
