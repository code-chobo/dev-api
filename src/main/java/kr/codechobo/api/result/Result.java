package kr.codechobo.api.result;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

public class Result {
    private Result(){}

    public static ResponseEntity<ApiResult> created() {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public static ResponseEntity<ApiResult> created(ApiResult payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(payload);
    }

    public static ResponseEntity<ApiResult> ok() {
        return ResponseEntity.ok().build();
    }

    public static ResponseEntity<ApiResult> ok(ApiResult payload) {
        Assert.notNull(payload, "Parameter `payload` must not be null");

        return ResponseEntity.ok(payload);
    }

    public static ResponseEntity<ApiResult> failure(String message) {
        return ResponseEntity.badRequest().body(ApiResult.message(message));
    }
}
