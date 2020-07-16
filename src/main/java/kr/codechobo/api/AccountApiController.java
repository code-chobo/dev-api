package kr.codechobo.api;

import kr.codechobo.account.AccountService;
import kr.codechobo.account.dto.JoinRequestDto;
import kr.codechobo.api.result.ApiResult;
import kr.codechobo.api.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/15
 */

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AccountApiController {

    private final AccountService accountService;

    @PostMapping("/account")
    public ResponseEntity<ApiResult> join(@RequestBody JoinRequestDto joinRequestDto) {
        Long joinId = accountService.join(joinRequestDto);

        return Result.created(ApiResult.blank().add("accountId", joinId));
    }
}
