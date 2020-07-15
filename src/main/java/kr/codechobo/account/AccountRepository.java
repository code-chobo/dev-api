package kr.codechobo.account;

import kr.codechobo.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/15
 */

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByNickname(String nickname);
}
