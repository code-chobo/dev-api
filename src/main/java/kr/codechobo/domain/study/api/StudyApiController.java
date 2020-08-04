package kr.codechobo.domain.study.api;

import kr.codechobo.domain.account.entity.CurrentAccount;
import kr.codechobo.domain.study.dto.CreateStudyRequest;
import kr.codechobo.domain.study.dto.JoinStudyRequest;
import kr.codechobo.domain.study.dto.StudyResponse;
import kr.codechobo.global.resultspec.ApiResult;
import kr.codechobo.global.resultspec.Result;
import kr.codechobo.domain.account.entity.Account;
import kr.codechobo.domain.study.entity.Study;
import kr.codechobo.domain.study.service.StudyService;
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
