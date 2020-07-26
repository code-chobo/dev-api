package kr.codechobo.domain;

import kr.codechobo.study.exception.AlreadyAcceptedException;
import kr.codechobo.study.exception.OverMaxEnrolmentException;
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

    private boolean accepted;

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

        return StudyAccount.builder()
                .account(account)
                .study(study)
                .refundBankAccount(refundBankAccount)
                .studentContact(studentContact)
                .canceledJoin(false)
                .studyRole(studyRole)
                .build();
    }

    public void acceptJoin() {
        if(study.getNumberOfMaxEnrolment() <= study.getNumberOfCurrentEnrolment()) {
            throw new OverMaxEnrolmentException(study.getNumberOfMaxEnrolment(), study.getNumberOfCurrentEnrolment());
        }

        if(accepted) {
           throw new AlreadyAcceptedException();
        }
        accepted = true;
        study.increaseNumberOfCurrentEnrolment();
    }

    public void canceledJoin() {
        if(accepted) {
            this.study.decreaseNumberOfCurrentEnrolment();
        }
        this.canceledJoin = true;
    }

    public String changeRefundBankAccount(String refundBankAccount) {
        this.refundBankAccount = refundBankAccount;
        return refundBankAccount;
    }

    public String changeStudentContact(String studentContact) {
        this.studentContact = studentContact;
        return studentContact;
    }


    public boolean isManager() {
        return studyRole.equals(StudyRole.MANAGER);
    }
}
