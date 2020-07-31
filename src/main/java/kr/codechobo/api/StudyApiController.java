package kr.codechobo.api;

import kr.codechobo.account.CurrentAccount;
import kr.codechobo.api.request.CreateStudyRequest;
import kr.codechobo.api.request.JoinStudyRequest;
import kr.codechobo.api.response.StudyResponse;
import kr.codechobo.api.result.ApiResult;
import kr.codechobo.api.result.Result;
import kr.codechobo.api.validator.CreateStudyRequestValidator;
import kr.codechobo.domain.Account;
import kr.codechobo.domain.Study;
import kr.codechobo.study.StudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */

@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@RestController
public class StudyApiController {

    private final CreateStudyRequestValidator createStudyRequestValidator;
    private final StudyService studyService;

    @InitBinder("createStudyRequest")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(createStudyRequestValidator);
    }

    @PostMapping("/study")
    public ResponseEntity<ApiResult> createStudy(@RequestBody @Validated CreateStudyRequest createStudyRequest,
                                                 @CurrentAccount Account managerAccount) {
        Long studyAccountId = studyService.createStudy(createStudyRequest, managerAccount);
        return Result.created(ApiResult.blank().add("studyAccountId", studyAccountId));
    }

    @PostMapping("/study/member")
    public ResponseEntity<ApiResult> joinStudy(@RequestBody @Validated JoinStudyRequest joinStudyRequest,
                                               @CurrentAccount Account account) {
        Long studyAccountId = studyService.joinStudy(joinStudyRequest, account);
        return Result.ok(ApiResult.blank().add("studyAccountId", studyAccountId));
    }

    @PatchMapping("/study/member/{studyAccountId}")
    public ResponseEntity<ApiResult> acceptJoinStudy(@CurrentAccount Account managerAccount,
                                                     @PathVariable Long studyAccountId) {
        studyService.acceptJoin(managerAccount, studyAccountId);
        return Result.ok(ApiResult.message("SUCCESS"));
    }

    @GetMapping("/study/{studyId}")
    public ResponseEntity<ApiResult> findStudy(@PathVariable Long studyId) {
        Study study = studyService.findStudyById(studyId);
        return StudyResponse.build(study);
    }

    @GetMapping("/study")
    public ResponseEntity<ApiResult> findMyStudies(@CurrentAccount Account account) {
        List<Study> myStudies = studyService.findMyStudies(account);
        return StudyResponse.build(myStudies);
    }

    @DeleteMapping("/study/{studyId}/member")
    public ResponseEntity<ApiResult> cancelJoinStudy(@PathVariable Long studyId,
                                                     @CurrentAccount Account account) {
        Long studyAccountId = studyService.cancelJoin(studyId, account);
        return Result.ok(ApiResult.blank().add("studyAccountId", studyAccountId));
    }

}
