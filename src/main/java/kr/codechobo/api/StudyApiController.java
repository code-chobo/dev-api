package kr.codechobo.api;

import kr.codechobo.account.CurrentAccount;
import kr.codechobo.api.request.CreateStudyRequest;
import kr.codechobo.api.request.JoinStudyRequest;
import kr.codechobo.api.result.ApiResult;
import kr.codechobo.api.result.Result;
import kr.codechobo.domain.Account;
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
    public ResponseEntity<ApiResult> createStudy(@RequestBody @Validated CreateStudyRequest createStudyRequest,
                                                 @CurrentAccount Account account) {
        Long studyId = studyService.createStudy(createStudyRequest, account);
        return Result.created(ApiResult.blank().add("studyId", studyId));
    }

    @PostMapping("/study/member")
    public ResponseEntity<ApiResult> joinStudy(@RequestBody @Validated JoinStudyRequest joinStudyRequest,
                                               @CurrentAccount Account account) {
        Long studyAccountId = studyService.joinStudy(joinStudyRequest, account);
        return Result.ok(ApiResult.blank().add("studyAccountId", studyAccountId));
    }

}
