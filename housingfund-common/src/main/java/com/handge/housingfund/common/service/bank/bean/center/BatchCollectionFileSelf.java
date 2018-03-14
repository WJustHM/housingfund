package com.handge.housingfund.common.service.bank.bean.center;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 批量收款文件,同行
 * 文件格式:序号@|$金额@|$付方账号@|$付方户名@|$摘要@|$业务明细账号@|$业务明细流水号@|$
 * Created by gxy on 17-8-3.
 */
public class BatchCollectionFileSelf implements Serializable {

    private static final long serialVersionUID = 6057399022466827468L;
    /**
     * 序号(required)
     */
    public String no = "";
    /**
     * 金额(required)
     */
    public BigDecimal amt;
    /**
     * 收方账户(required)
     */
    public String deAcctNo = "";
    /**
     * 收方户名(required)
     */
    public String deAcctName = "";
    /**
     * 摘要(required)
     */
    public String summary = "";
    /**
     * 业务明细账号
     */
    public String refAcctNo = "";
    /**
     * 业务明细流水号
     */
    public String refSeqNo = "";

    public BatchCollectionFileSelf() {
    }

    public BatchCollectionFileSelf(String no, BigDecimal amt, String deAcctNo, String deAcctName, String summary) {
        this.no = no;
        this.amt = amt;
        this.deAcctNo = deAcctNo;
        this.deAcctName = deAcctName;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRefAcctNo() {
        return refAcctNo;
    }

    public void setRefAcctNo(String refAcctNo) {
        this.refAcctNo = refAcctNo;
    }

    public String getRefSeqNo() {
        return refSeqNo;
    }

    public void setRefSeqNo(String refSeqNo) {
        this.refSeqNo = refSeqNo;
    }

    @Override
    public String toString() {
        return no + "@|$" +
                getString(amt) + "@|$" +
                deAcctNo + "@|$" +
                deAcctName + "@|$" +
                summary + "@|$" +
                refAcctNo + "@|$" +
                refSeqNo + "@|$";
    }

    public String getString(BigDecimal decimal) {
        return decimal != null ? decimal.toString() : "";
    }
}
