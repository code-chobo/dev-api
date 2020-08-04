package kr.codechobo.domain.account.entity;

import kr.codechobo.domain.account.entity.Account;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/17
 */

@Getter
public class AccountAdapter extends User {

    private final Account account;

    public AccountAdapter(Account account) {
        super(account.getEmail(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_"+account.getRole().name())));
        this.account = account;
    }

}
