package kr.codechobo.api;

import kr.codechobo.account.CurrentAccount;
import kr.codechobo.api.request.CreateStudyRequest;
import kr.codechobo.api.request.JoinStudyRequest;
import kr.codechobo.api.response.StudyResponse;
import kr.codechobo.api.result.ApiResult;
import kr.codechobo.api.result.Result;
import kr.codechobo.domain.Account;
import kr.codechobo.domain.Study;
import kr.codechobo.study.StudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/study/{studyId}")
    public ResponseEntity<ApiResult> findStudy(@PathVariable Long studyId) {
        Study study = studyService.findStudyById(studyId);
        return StudyResponse.build(study);
    }

    @DeleteMapping("/study/{studyId}/member")
    public ResponseEntity<ApiResult> cancelJoinStudy(@PathVariable Long studyId,
                                                     @CurrentAccount Account account) {
        Long studyAccountId = studyService.cancelJoin(studyId, account);
        return Result.ok(ApiResult.blank().add("studyAccountId", studyAccountId));
    }

}
