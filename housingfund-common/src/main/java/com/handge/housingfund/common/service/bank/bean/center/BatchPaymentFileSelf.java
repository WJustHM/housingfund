package com.handge.housingfund.common.service.bank.bean.center;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 批量付款文件,同行
 * 文件格式:序号@|$金额@|$收方账户@|$收方户名@|$摘要@|$业务明细账号@|$业务明细流水号 1@|$业务明细流水号 2@|$本金发生额@|$利息发生额@|$
 * Created by gxy on 17-8-2.
 */
public class BatchPaymentFileSelf implements Serializable {
    private static final long serialVersionUID = -97033345757659888L;
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
    public String crAcctNo = "";
    /**
     * 收方户名(required)
     */
    public String crAcctName = "";
    /**
     * 摘要(required)
     */
    public String summary = "";
    /**
     * 业务明细账号
     */
    public String refAcctNo = "";
    /**
     * 业务明细流水号 1(required)
     */
    public String refSeqNo1 = "";
    /**
     * 业务明细流水号 2
     */
    public String refSeqNo2 = "";
    /**
     * 本金发生额(required)
     */
    public BigDecimal capAmt;
    /**
     * 利息发生额
     */
    public BigDecimal intAmt;

    public BatchPaymentFileSelf() {
    }

    public BatchPaymentFileSelf(String no, BigDecimal amt, String crAcctNo, String crAcctName, String summary, String refSeqNo1, BigDecimal capAmt) {
        this.no = no;
        this.amt = amt;
        this.crAcctNo = crAcctNo;
        this.crAcctName = crAcctName;
        this.summary = summary;
        this.refSeqNo1 = refSeqNo1;
        this.capAmt = capAmt;
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

    public String getCrAcctNo() {
        return crAcctNo;
    }

    public void setCrAcctNo(String crAcctNo) {
        this.crAcctNo = crAcctNo;
    }

    public String getCrAcctName() {
        return crAcctName;
    }

    public void setCrAcctName(String crAcctName) {
        this.crAcctName = crAcctName;
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

    public String getRefSeqNo1() {
        return refSeqNo1;
    }

    public void setRefSeqNo1(String refSeqNo1) {
        this.refSeqNo1 = refSeqNo1;
    }

    public String getRefSeqNo2() {
        return refSeqNo2;
    }

    public void setRefSeqNo2(String refSeqNo2) {
        this.refSeqNo2 = refSeqNo2;
    }

    public BigDecimal getCapAmt() {
        return capAmt;
    }

    public void setCapAmt(BigDecimal capAmt) {
        this.capAmt = capAmt;
    }

    public BigDecimal getIntAmt() {
        return intAmt;
    }

    public void setIntAmt(BigDecimal intAmt) {
        this.intAmt = intAmt;
    }

    @Override
    public String toString() {
        return no + "@|$" +
                getString(amt) + "@|$" +
                crAcctNo + "@|$" +
                crAcctName + "@|$" +
                summary + "@|$" +
                refAcctNo + "@|$" +
                refSeqNo1 + "@|$" +
                refSeqNo2 + "@|$" +
                getString(capAmt) + "@|$" +
                getString(intAmt) + "@|$";
    }

    public String getString(BigDecimal decimal) {
        return decimal != null ? decimal.toString() : "";
    }
}
