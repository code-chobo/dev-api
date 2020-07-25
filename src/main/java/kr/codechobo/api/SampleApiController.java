package kr.codechobo.api;

import kr.codechobo.account.AccountAdapter;
import kr.codechobo.account.CurrentAccount;
import kr.codechobo.api.result.ApiResult;
import kr.codechobo.api.result.Result;
import kr.codechobo.domain.Account;
import kr.codechobo.domain.AccountRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
