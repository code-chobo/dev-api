package kr.codechobo.api;

import kr.codechobo.account.AccountService;
import kr.codechobo.api.request.JoinAccountRequest;
import kr.codechobo.api.result.ApiResult;
import kr.codechobo.api.result.Result;
import kr.codechobo.api.validator.JoinAccountRequestValidator;
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
