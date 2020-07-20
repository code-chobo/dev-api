package kr.codechobo.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDateTime;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */

@NoArgsConstructor
@Getter
public class StudyCreateRequest {

    private String title;
    private String description;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int numberOfMaxEnrolment;
    private int numberOfMinEnrolment;
    private String bankAccount;
    private String leaderContact;

    @Builder
    public StudyCreateRequest(String title, String description, String location, LocalDateTime startDate, LocalDateTime endDate, int numberOfMaxEnrolment, int numberOfMinEnrolment, String bankAccount, String leaderContact) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfMaxEnrolment = numberOfMaxEnrolment;
        this.numberOfMinEnrolment = numberOfMinEnrolment;
        this.bankAccount = bankAccount;
        this.leaderContact = leaderContact;
    }

    @AssertTrue
    public boolean isStartDateBeforeThanEndDate() {
        return startDate.isBefore(endDate);
    }

    @AssertTrue
    public boolean isMinEnrolmentLessThanMaxEnrolment() {
        return numberOfMinEnrolment <= numberOfMaxEnrolment;
    }
}
