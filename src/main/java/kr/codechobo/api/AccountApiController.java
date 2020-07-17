package kr.codechobo.api;

import kr.codechobo.account.AccountService;
import kr.codechobo.api.request.JoinRequest;
import kr.codechobo.api.result.ApiResult;
import kr.codechobo.api.result.Result;
import kr.codechobo.api.validator.JoinRequestValidator;
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
    private final JoinRequestValidator joinRequestValidator;

    @InitBinder("joinRequest")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(joinRequestValidator);
    }

    @PostMapping("/account")
    public ResponseEntity<ApiResult> join(@RequestBody @Validated JoinRequest joinRequest) {
        Long joinId = accountService.join(joinRequest);

        return Result.created(ApiResult.blank().add("accountId", joinId));
    }
}
