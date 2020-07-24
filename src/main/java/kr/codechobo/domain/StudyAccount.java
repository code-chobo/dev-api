package kr.codechobo.domain;

import lombok.*;

import javax.persistence.*;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */

@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class StudyAccount extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_account_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    private String refundBankAccount;

    private String studentContact;

    private boolean canceledJoin;

    @Enumerated(EnumType.STRING)
    private StudyRole studyRole;

    @Builder
    public StudyAccount(Long id, Account account, Study study, String refundBankAccount, String studentContact, boolean canceledJoin, StudyRole studyRole) {
        this.id = id;
        this.account = account;
        this.study = study;
        this.refundBankAccount = refundBankAccount;
        this.studentContact = studentContact;
        this.canceledJoin = canceledJoin;
        this.studyRole = studyRole;
    }

    public static StudyAccount CreateStudyAccount(Account account, Study study, String refundBankAccount, String studentContact, StudyRole studyRole) {
        study.increaseNumberOfCurrentEnrolment();
        return StudyAccount.builder()
                .account(account)
                .study(study)
                .refundBankAccount(refundBankAccount)
                .studentContact(studentContact)
                .canceledJoin(false)
                .studyRole(studyRole)
                .build();
    }

    public String changeRefundBankAccount(String refundBankAccount) {
        this.refundBankAccount = refundBankAccount;
        return refundBankAccount;
    }

    public String changeStudentContact(String studentContact) {
        this.studentContact = studentContact;
        return studentContact;
    }

    public void canceledJoin() {
        this.canceledJoin = true;
        this.study.decreaseNumberOfCurrentEnrolment();
    }
}
