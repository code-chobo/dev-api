package kr.codechobo.global.resultspec;

import java.util.HashMap;

public class ApiResult extends HashMap<String, Object> {

    public static ApiResult blank() {
        return new ApiResult();
    }

    public static ApiResult message(String message) {

        ApiResult apiResult = new ApiResult();
        apiResult.put("message", message);
        return apiResult;
    }

    public ApiResult add(String key, Object value) {
        this.put(key, value);
        return this;
    }
}
