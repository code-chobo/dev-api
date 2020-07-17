package kr.codechobo.account;

import kr.codechobo.account.exception.AccountNotFoundException;
import kr.codechobo.account.exception.PasswordWrongException;
import kr.codechobo.api.request.JoinRequest;
import kr.codechobo.config.security.TokenManager;
import kr.codechobo.domain.Account;
import kr.codechobo.domain.AccountRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/15
 */

@RequiredArgsConstructor
@Transactional
@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenManager tokenManager;

    public Account findAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    public Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email.toLowerCase()).orElseThrow(() -> new AccountNotFoundException(email));
    }

    public Long join(JoinRequest joinRequest) {
        Account newAccount = Account.builder()
                .email(joinRequest.getEmail().toLowerCase())
                .nickname(joinRequest.getNickname())
                .password(passwordEncoder.encode(joinRequest.getPassword()))
                .role(AccountRole.COMMON)
                .build();

        accountRepository.save(newAccount);

        return newAccount.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return new User(account.getEmail(), account.getPassword(), List.of(new SimpleGrantedAuthority(account.getRole().name())));
    }

    public String authenticate(String email, String password) {
        Account account = findAccountByEmail(email);
        boolean matches = passwordEncoder.matches(password, account.getPassword());

        if(!matches) {
            throw new PasswordWrongException();
        }

        return tokenManager.createToken(account.getNickname());
    }
}
