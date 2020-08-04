package kr.codechobo.global;

import kr.codechobo.domain.account.entity.CurrentAccount;
import kr.codechobo.global.resultspec.ApiResult;
import kr.codechobo.global.resultspec.Result;
import kr.codechobo.domain.account.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/19
 */

@Slf4j
@RequestMapping("/api")
@RestController
public class SampleApiController {

    @PreAuthorize("hasAuthority('COMMON')")
    @GetMapping("/info/common")
    public String infoCommonAccount(@CurrentAccount Account account) {
        log.info(account.toString());
        return "COMMON ACCOUNT";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/info/admin")
    public String infoAdminAccount(@CurrentAccount Account account) {
        log.info(account.toString());
        return "ADMIN ACCOUNT";
    }

    @GetMapping("/sample")
    public ResponseEntity<ApiResult> sample() {
        return Result.ok(ApiResult.message("OK"));
    }
}
