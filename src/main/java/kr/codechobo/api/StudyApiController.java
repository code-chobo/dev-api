package kr.codechobo.api;

import kr.codechobo.api.request.StudyCreateRequest;
import kr.codechobo.api.result.ApiResult;
import kr.codechobo.study.StudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */

@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@RestController
public class StudyApiController {

    private final StudyService studyService;

    @PostMapping("/study")
    public ResponseEntity<ApiResult> createStudy(@RequestBody @Validated StudyCreateRequest studyCreateRequest) {
        return null;
    }

}
