package kr.codechobo.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import kr.codechobo.api.result.ApiResult;
import kr.codechobo.api.result.Result;
import kr.codechobo.domain.Location;
import kr.codechobo.domain.Study;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/22
 */

@Getter
public class StudyResponse {
    public static ResponseEntity<ApiResult> build(List<Study> studies) {
        List<StudyData> studyDataList = new ArrayList<>(studies.size());
        for (Study study : studies) {
            StudyData studyData = buildStudy(study);
            studyDataList.add(studyData);
        }
        return result("studies", studyDataList);
    }


    public static ResponseEntity<ApiResult> build(Study study) {
        StudyData studyData = buildStudy(study);
        return result("study", studyData);
    }

    private static StudyData buildStudy(Study study) {
        return StudyData.builder()
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
                .leaderContact(study.getLeaderContact())
                .createdBy(study.getCreatedBy())
                .createdDate(study.getCreatedDate())
                .modifiedDate(study.getModifiedDate())
                .build();
    }

    private static ResponseEntity<ApiResult> result(String key, Object o) {
        ApiResult result = ApiResult.blank()
                .add(key, o);
        return Result.ok(result);
    }

    @ToString
    @Getter
    private static class StudyData {
        private Long id;
        private String title;
        private String description;
        private Location location;
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd kk:mm")
        private LocalDateTime startDate;
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd kk:mm")
        private LocalDateTime endDate;
        private int numberOfMaxEnrolment;
        private int numberOfMinEnrolment;
        private int numberOfCurrentEnrolment;
        private boolean closed;
        private String bankAccount;
        private String leaderContact;
        private String createdBy;
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
        private LocalDateTime createdDate;
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
        private LocalDateTime modifiedDate;

        @Builder
        public StudyData(Long id, String title, String description, Location location, LocalDateTime startDate, LocalDateTime endDate, int numberOfMaxEnrolment, int numberOfMinEnrolment, int numberOfCurrentEnrolment, boolean closed, String bankAccount, String leaderContact, String createdBy, LocalDateTime createdDate, LocalDateTime modifiedDate) {
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
            this.createdDate = createdDate;
            this.modifiedDate = modifiedDate;
        }
    }
}
