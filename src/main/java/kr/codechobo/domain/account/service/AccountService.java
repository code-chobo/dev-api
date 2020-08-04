package kr.codechobo.domain.account.service;

import kr.codechobo.domain.account.entity.AccountAdapter;
import kr.codechobo.domain.account.repository.AccountRepository;
import kr.codechobo.domain.account.exception.AccountNotFoundException;
import kr.codechobo.domain.account.dto.JoinAccountRequest;
import kr.codechobo.domain.account.entity.Account;
import kr.codechobo.domain.account.entity.AccountRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email.toLowerCase()).orElseThrow(() -> new AccountNotFoundException(email));
    }

    public Long join(JoinAccountRequest joinAccountRequest) {
        Account newAccount = Account.builder()
                .email(joinAccountRequest.getEmail().toLowerCase())
                .nickname(joinAccountRequest.getNickname())
                .password(passwordEncoder.encode(joinAccountRequest.getPassword()))
                .role(AccountRole.COMMON)
                .build();

        accountRepository.save(newAccount);

        return newAccount.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return new AccountAdapter(account);
    }
}
