package kr.codechobo.api;

import kr.codechobo.account.AccountService;
import kr.codechobo.api.request.AuthRequest;
import kr.codechobo.api.result.ApiResult;
import kr.codechobo.api.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/17
 */


@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AuthenticateApiController {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final AccountService accountService;

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResult> create(@RequestBody AuthRequest authRequest) {

        String email = authRequest.getEmail();
        String password = authRequest.getPassword();

        String token = accountService.authenticate(email, password);

        return Result.createdWithHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token);
    }

}
