package kr.codechobo.domain.account.api;

import kr.codechobo.domain.account.service.AccountService;
import kr.codechobo.domain.account.dto.JoinAccountRequest;
import kr.codechobo.global.resultspec.ApiResult;
import kr.codechobo.global.resultspec.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/15
 */

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AccountApiController {

    private final AccountService accountService;
    private final JoinAccountRequestValidator joinAccountRequestValidator;

    @InitBinder("joinAccountRequest")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(joinAccountRequestValidator);
    }

    @PostMapping("/account")
    public ResponseEntity<ApiResult> join(@RequestBody @Validated JoinAccountRequest joinAccountRequest) {
        Long joinId = accountService.join(joinAccountRequest);
        return Result.created(ApiResult.blank().add("accountId", joinId));
    }
}
