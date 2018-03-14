package com.handge.housingfund.common.service.bank.bean.center;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 贷款扣款文件,跨行和混合
 * 文件格式:序号@|$金额@|$付方账号@|$付方户名@|$付方联行号@|$付方开户行联行号@|$付方地址(可选)@|$多方协议号@|$足额标志@|$摘要@|$
 * Created by gxy on 17-8-3.
 */
public class LoanDeductionFileOther extends LoanDeductionFileSelf implements Serializable {

    private static final long serialVersionUID = 6196240177244877552L;
    /**
     * 付方联行号(required)
     */
    private String deChgNo = "";
    /**
     * 付方开户行联行号
     */
    private String deBankChgNo = "";
    /**
     * 付方地址(可选)
     */
    private String address = "";
    /**
     * 多方协议号
     */
    private String conAgrNo = "";

    public LoanDeductionFileOther() {
    }

    public LoanDeductionFileOther(String no, BigDecimal amt, String deAcctNo, String deAcctName, String deChgNo, String fullMark, String summary) {
        super(no, amt, deAcctNo, deAcctName, fullMark, summary);
        this.deChgNo = deChgNo;
    }

    public String getDeChgNo() {
        return deChgNo;
    }

    public void setDeChgNo(String deChgNo) {
        this.deChgNo = deChgNo;
    }

    public String getDeBankChgNo() {
        return deBankChgNo;
    }

    public void setDeBankChgNo(String deBankChgNo) {
        this.deBankChgNo = deBankChgNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConAgrNo() {
        return conAgrNo;
    }

    public void setConAgrNo(String conAgrNo) {
        this.conAgrNo = conAgrNo;
    }

    @Override
    public String toString() {
        return no + "@|$" +
                getString(amt) + "@|$" +
                deAcctNo + "@|$" +
                deAcctName + "@|$" +
                deChgNo + "@|$" +
                deBankChgNo + "@|$" +
                address + "@|$" +
                conAgrNo + "@|$" +
                fullMark + "@|$" +
                summary + "@|$";
    }
}
