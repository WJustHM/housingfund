package com.handge.housingfund.common.service.bank.bean.center;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 贷款扣款文件,同行
 * 文件格式:序号@|$金额@|$付方账号@|$付方户名@|$足额标志@|$摘要@|$
 * Created by gxy on 17-8-3.
 */
public class LoanDeductionFileSelf implements Serializable {
    private static final long serialVersionUID = 2131455987951371415L;
    /**
     * 序号(required)
     */
    public String no = "";
    /**
     * 金额(required)
     */
    public BigDecimal amt;
    /**
     * 付方账号(required)
     */
    public String deAcctNo = "";
    /**
     * 付方户名(required)
     */
    public String deAcctName = "";
    /**
     * 足额标志(required)
     */
    public String fullMark = "";
    /**
     * 摘要(required)
     */
    public String summary = "";

    public LoanDeductionFileSelf() {
    }

    public LoanDeductionFileSelf(String no, BigDecimal amt, String deAcctNo, String deAcctName, String fullMark, String summary) {
        this.no = no;
        this.amt = amt;
        this.deAcctNo = deAcctNo;
        this.deAcctName = deAcctName;
        this.fullMark = fullMark;
        this.summary = summary;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public String getDeAcctNo() {
        return deAcctNo;
    }

    public void setDeAcctNo(String deAcctNo) {
        this.deAcctNo = deAcctNo;
    }

    public String getDeAcctName() {
        return deAcctName;
    }

    public void setDeAcctName(String deAcctName) {
        this.deAcctName = deAcctName;
    }

    public String getFullMark() {
        return fullMark;
    }

    public void setFullMark(String fullMark) {
        this.fullMark = fullMark;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return no + "@|$" +
                getString(amt) + "@|$" +
                deAcctNo + "@|$" +
                deAcctName + "@|$" +
                fullMark + "@|$" +
                summary + "@|$";
    }

    public String getString(BigDecimal decimal) {
        return decimal != null ? decimal.toString() : "";
    }
}
