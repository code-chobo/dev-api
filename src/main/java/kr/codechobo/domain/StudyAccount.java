package kr.codechobo.domain;

import lombok.*;

import javax.persistence.*;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */

@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class StudyAccount {

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

    @Builder
    public StudyAccount(Account account, Study study, String refundBankAccount, String studentContact) {
        this.account = account;
        this.study = study;
        this.refundBankAccount = refundBankAccount;
        this.studentContact = studentContact;
    }

    public String changeRefundBankAccount(String refundBankAccount) {
        this.refundBankAccount = refundBankAccount;
        return refundBankAccount;
    }

    public String changeStudentContact(String studentContact) {
        this.studentContact = studentContact;
        return studentContact;
    }
}
