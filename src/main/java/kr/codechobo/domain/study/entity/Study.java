package kr.codechobo.domain.study.entity;

import kr.codechobo.global.baseentity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */

@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Study extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    private String title;

    @Lob
    private String description;

    @Embedded
    private Location location;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int numberOfMaxEnrolment;

    private int numberOfMinEnrolment;

    private int numberOfCurrentEnrolment;

    private boolean closed;

    private String bankAccount;

    private String leaderContact;

    @Builder
    public Study(String title, String description, Location location,
                 LocalDateTime startDate, LocalDateTime endDate,
                 int numberOfMaxEnrolment, int numberOfMinEnrolment,
                 boolean closed, String bankAccount, String leaderContact) {
        this.title = title;
        this.description = description;
        this.location = location;

        this.startDate = startDate;
        this.endDate = endDate;

        this.numberOfMaxEnrolment = numberOfMaxEnrolment;
        this.numberOfMinEnrolment = numberOfMinEnrolment;

        this.closed = closed;
        this.bankAccount = bankAccount;

        this.leaderContact = leaderContact;

    }

    public static Study createStudy(String title, String description, Location location,
                                    LocalDateTime startDate, LocalDateTime endDate,
                                    int numberOfMaxEnrolment, int numberOfMinEnrolment,
                                    String bankAccount, String leaderContact) {
        return Study.builder()
                .title(title)
                .description(description)
                .location(location)
                .startDate(startDate)
                .endDate(endDate)
                .numberOfMaxEnrolment(numberOfMaxEnrolment)
                .numberOfMinEnrolment(numberOfMinEnrolment)
                .closed(false)
                .bankAccount(bankAccount)
                .leaderContact(leaderContact)
                .build();
    }

    public void increaseNumberOfCurrentEnrolment() {
        this.numberOfCurrentEnrolment++;
    }

    public void decreaseNumberOfCurrentEnrolment() {
        this.numberOfCurrentEnrolment--;
    }

}
