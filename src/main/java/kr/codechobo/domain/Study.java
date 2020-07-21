package kr.codechobo.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */

@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Study {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    private String title;

    @Lob
    private String description;

    private String location;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int numberOfMaxEnrolment;

    private int numberOfMinEnrolment;

    private int numberOfCurrentEnrolment;

    private boolean closed;

    private String bankAccount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account manager;

    @Builder
    public Study(Long id, String title, String description, String location,
                 LocalDateTime startDate, LocalDateTime endDate,
                 int numberOfMaxEnrolment, int numberOfMinEnrolment,
                 boolean closed, String bankAccount,
                 Account manager) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;

        this.startDate = startDate;
        this.endDate = endDate;

        this.numberOfMaxEnrolment = numberOfMaxEnrolment;
        this.numberOfMinEnrolment = numberOfMinEnrolment;

        this.closed = closed;
        this.bankAccount = bankAccount;

        this.manager = manager;
    }

    public static Study createStudy(String title, String description, String location,
                                    LocalDateTime startDate, LocalDateTime endDate,
                                    int numberOfMaxEnrolment, int numberOfMinEnrolment,
                                    String bankAccount, Account manager) {
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
                .manager(manager)
                .build();
    }

}
