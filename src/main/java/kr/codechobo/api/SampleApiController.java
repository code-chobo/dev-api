package kr.codechobo.api;

import kr.codechobo.account.AccountAdapter;
import kr.codechobo.domain.AccountRole;
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

@RequestMapping("/api")
@RestController
public class SampleApiController {

    @PreAuthorize("hasAuthority('COMMON')")
    @GetMapping("/info/common")
    public String infoCommonAccount(@AuthenticationPrincipal AccountAdapter accountAdapter) {
        System.out.println(accountAdapter);
        return "COMMON ACCOUNT";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/info/admin")
    public String infoAdminAccount(@AuthenticationPrincipal AccountAdapter accountAdapter) {
        System.out.println(accountAdapter);
        return "ADMIN ACCOUNT";
    }
}
