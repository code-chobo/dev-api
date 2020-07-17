package kr.codechobo.account;

import kr.codechobo.domain.Account;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/17
 */

@Getter
public class SessionAccount extends User {

    private Account account;

    public SessionAccount(Account account) {
        super(account.getEmail(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_"+account.getRole().name())));
        this.account = account;
    }
}
