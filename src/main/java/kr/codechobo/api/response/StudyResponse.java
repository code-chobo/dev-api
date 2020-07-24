package kr.codechobo.api.response;

import kr.codechobo.api.result.ApiResult;
import kr.codechobo.api.result.Result;
import kr.codechobo.domain.Account;
import kr.codechobo.domain.Study;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/22
 */

@Getter
public class StudyResponse {

    public static ResponseEntity<ApiResult> build(Study study) {
        StudyData studyData = StudyData.builder()
                .id(study.getId())
                .title(study.getTitle())
                .description(study.getDescription())
                .location(study.getLocation())
                .startDate(study.getStartDate())
                .endDate(study.getEndDate())
                .numberOfMaxEnrolment(study.getNumberOfMaxEnrolment())
                .numberOfMinEnrolment(study.getNumberOfMinEnrolment())
                .numberOfCurrentEnrolment(study.getNumberOfCurrentEnrolment())
                .closed(study.isClosed())
                .bankAccount(study.getBankAccount())
                .createdBy(study.getCreatedBy().getNickname())
                .build();


        ApiResult result = ApiResult.blank()
                .add("study", studyData);
        return Result.ok(result);
    }

    @Getter
    private static class StudyData {
        private Long id;
        private String title;
        private String description;
        private String location;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private int numberOfMaxEnrolment;
        private int numberOfMinEnrolment;
        private int numberOfCurrentEnrolment;
        private boolean closed;
        private String bankAccount;
        private String leaderContact;
        private String createdBy;

        @Builder
        public StudyData(Long id, String title, String description, String location, LocalDateTime startDate, LocalDateTime endDate, int numberOfMaxEnrolment, int numberOfMinEnrolment, int numberOfCurrentEnrolment, boolean closed, String bankAccount, String leaderContact, String createdBy) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.location = location;
            this.startDate = startDate;
            this.endDate = endDate;
            this.numberOfMaxEnrolment = numberOfMaxEnrolment;
            this.numberOfMinEnrolment = numberOfMinEnrolment;
            this.numberOfCurrentEnrolment = numberOfCurrentEnrolment;
            this.closed = closed;
            this.bankAccount = bankAccount;
            this.leaderContact = leaderContact;
            this.createdBy = createdBy;
        }
    }
}
