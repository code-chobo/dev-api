package kr.codechobo.domain.study.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/21
 */

@NoArgsConstructor
@Getter
public class JoinStudyRequest {
    private Long studyId;
    private String refundBankAccount;
    private String studentContact;

    public JoinStudyRequest(Long studyId, String refundBankAccount, String studentContact) {
        this.studyId = studyId;
        this.refundBankAccount = refundBankAccount;
        this.studentContact = studentContact;
    }
}
