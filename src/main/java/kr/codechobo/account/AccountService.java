package kr.codechobo.account;

import kr.codechobo.account.dto.JoinRequestDto;
import kr.codechobo.account.exception.AccountNotFoundException;
import kr.codechobo.account.exception.ExistsEmailException;
import kr.codechobo.account.exception.ExistsNicknameException;
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

    public Account findAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    public Long join(JoinRequestDto joinRequestDto) {
        isUniqueEmail(joinRequestDto.getEmail());
        isUniqueNickname(joinRequestDto.getNickname());

        Account newAccount = Account.builder()
                .email(joinRequestDto.getEmail())
                .nickname(joinRequestDto.getNickname())
                .password(passwordEncoder.encode(joinRequestDto.getPassword()))
                .role(AccountRole.COMMON)
                .build();

        accountRepository.save(newAccount);

        return newAccount.getId();
    }

    private void isUniqueNickname(String nickname) {
        if(accountRepository.existsByNickname(nickname)) {
            throw new ExistsNicknameException(nickname);
        }
    }

    private void isUniqueEmail(String email) {
        if(accountRepository.existsByEmail(email)) {
            throw new ExistsEmailException(email);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return new User(account.getEmail(), account.getPassword(), List.of(new SimpleGrantedAuthority(account.getRole().name())));
    }
}
