package com.handge.housingfund.common.service.bank.bean.center;


import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 单笔付款(BDC101)输入格式
 */
public class SinglePaymentIn implements Serializable{
    private static final long serialVersionUID = -7414030064634749623L;
    /**
     * CenterHeadIn(required)
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 结算模式(required)
     */
    private String SettleType;
    /**
     * 业务类型(required)
     */
    private String BusType;
    /**
     * 付方账号(required)
     */
    private String DeAcctNo;
    /**
     * 付方户名(required)
     */
    private String DeAcctName;
    /**
     * 付方账户类别(required)
     */
    private String DeAcctClass;
    /**
     * 本金发生额(required)
     */
    private BigDecimal CapAmt;
    /**
     * 付息户账号
     */
    private String DeIntAcctNo;
    /**
     * 付息户户名
     */
    private String DeIntAcctName;
    /**
     * 付息户类别
     */
    private String DeIntAcctClass;
    /**
     * 利息收方账号
     */
    private String DeIntCrAcct;
    /**
     * 利息发生额
     */
    private BigDecimal IntAmt;
    /**
     * 收方账号(required)
     */
    private String CrAcctNo;
    /**
     * 收方户名(required)
     */
    private String CrAcctName;
    /**
     * 收方账户类别(required)
     */
    private String CrAcctClass;
    /**
     * 收方账户行别(required)
     */
    private String CrBankClass;
    /**
     * 收方行名
     */
    private String CrBankName;
    /**
     * 收方联行号
     */
    private String CrChgNo;
    /**
     * 金额(required)
     */
    private BigDecimal Amt;
    /**
     * 业务明细账号
     */
    private String RefAcctNo;
    /**
     * 业务明细流水号1(required)
     */
    private String RefSeqNo1;
    /**
     * 业务明细流水号2
     */
    private String RefSeqNo2;
    /**
     * 摘要(required)
     */
    private String Summary;
    /**
     * 备注(required)
     */
    private String Remark;

    public SinglePaymentIn() {
    }

    public SinglePaymentIn(CenterHeadIn centerHeadIn, String settleType, String busType, String deAcctNo, String deAcctName, String deAcctClass, BigDecimal capAmt, String crAcctNo, String crAcctName, String crAcctClass, String crBankClass, BigDecimal amt, String refSeqNo1, String summary, String remark) {
        this.centerHeadIn = centerHeadIn;
        SettleType = settleType;
        BusType = busType;
        DeAcctNo = deAcctNo;
        DeAcctName = deAcctName;
        DeAcctClass = deAcctClass;
        CapAmt = capAmt;
        CrAcctNo = crAcctNo;
        CrAcctName = crAcctName;
        CrAcctClass = crAcctClass;
        CrBankClass = crBankClass;
        Amt = amt;
        RefSeqNo1 = refSeqNo1;
        Summary = summary;
        Remark = remark;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public String getSettleType() {
        return SettleType;
    }

    public void setSettleType(String settleType) {
        SettleType = settleType;
    }

    public String getBusType() {
        return BusType;
    }

    public void setBusType(String busType) {
        BusType = busType;
    }

    public String getDeAcctNo() {
        return DeAcctNo;
    }

    public void setDeAcctNo(String deAcctNo) {
        DeAcctNo = deAcctNo;
    }

    public String getDeAcctName() {
        return DeAcctName;
    }

    public void setDeAcctName(String deAcctName) {
        DeAcctName = deAcctName;
    }

    public String getDeAcctClass() {
        return DeAcctClass;
    }

    public void setDeAcctClass(String deAcctClass) {
        DeAcctClass = deAcctClass;
    }

    public BigDecimal getCapAmt() {
        return CapAmt;
    }

    public void setCapAmt(BigDecimal capAmt) {
        CapAmt = capAmt;
    }

    public String getDeIntAcctNo() {
        return DeIntAcctNo;
    }

    public void setDeIntAcctNo(String deIntAcctNo) {
        DeIntAcctNo = deIntAcctNo;
    }

    public String getDeIntAcctName() {
        return DeIntAcctName;
    }

    public void setDeIntAcctName(String deIntAcctName) {
        DeIntAcctName = deIntAcctName;
    }

    public String getDeIntAcctClass() {
        return DeIntAcctClass;
    }

    public void setDeIntAcctClass(String deIntAcctClass) {
        DeIntAcctClass = deIntAcctClass;
    }

    public String getDeIntCrAcct() {
        return DeIntCrAcct;
    }

    public void setDeIntCrAcct(String deIntCrAcct) {
        DeIntCrAcct = deIntCrAcct;
    }

    public BigDecimal getIntAmt() {
        return IntAmt;
    }

    public void setIntAmt(BigDecimal intAmt) {
        IntAmt = intAmt;
    }

    public String getCrAcctNo() {
        return CrAcctNo;
    }

    public void setCrAcctNo(String crAcctNo) {
        CrAcctNo = crAcctNo;
    }

    public String getCrAcctName() {
        return CrAcctName;
    }

    public void setCrAcctName(String crAcctName) {
        CrAcctName = crAcctName;
    }

    public String getCrAcctClass() {
        return CrAcctClass;
    }

    public void setCrAcctClass(String crAcctClass) {
        CrAcctClass = crAcctClass;
    }

    public String getCrBankClass() {
        return CrBankClass;
    }

    public void setCrBankClass(String crBankClass) {
        CrBankClass = crBankClass;
    }

    public String getCrBankName() {
        return CrBankName;
    }

    public void setCrBankName(String crBankName) {
        CrBankName = crBankName;
    }

    public String getCrChgNo() {
        return CrChgNo;
    }

    public void setCrChgNo(String crChgNo) {
        CrChgNo = crChgNo;
    }

    public BigDecimal getAmt() {
        return Amt;
    }

    public void setAmt(BigDecimal amt) {
        Amt = amt;
    }

    public String getRefAcctNo() {
        return RefAcctNo;
    }

    public void setRefAcctNo(String refAcctNo) {
        RefAcctNo = refAcctNo;
    }

    public String getRefSeqNo1() {
        return RefSeqNo1;
    }

    public void setRefSeqNo1(String refSeqNo1) {
        RefSeqNo1 = refSeqNo1;
    }

    public String getRefSeqNo2() {
        return RefSeqNo2;
    }

    public void setRefSeqNo2(String refSeqNo2) {
        RefSeqNo2 = refSeqNo2;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    @Override
    public String toString() {
        return "SinglePaymentIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", SettleType='" + SettleType + '\'' +
                ", BusType='" + BusType + '\'' +
                ", DeAcctNo='" + DeAcctNo + '\'' +
                ", DeAcctName='" + DeAcctName + '\'' +
                ", DeAcctClass='" + DeAcctClass + '\'' +
                ", CapAmt=" + CapAmt +
                ", DeIntAcctNo='" + DeIntAcctNo + '\'' +
                ", DeIntAcctName='" + DeIntAcctName + '\'' +
                ", DeIntAcctClass='" + DeIntAcctClass + '\'' +
                ", DeIntCrAcct='" + DeIntCrAcct + '\'' +
                ", IntAmt=" + IntAmt +
                ", CrAcctNo='" + CrAcctNo + '\'' +
                ", CrAcctName='" + CrAcctName + '\'' +
                ", CrAcctClass='" + CrAcctClass + '\'' +
                ", CrBankClass='" + CrBankClass + '\'' +
                ", CrBankName='" + CrBankName + '\'' +
                ", CrChgNo='" + CrChgNo + '\'' +
                ", Amt=" + Amt +
                ", RefAcctNo='" + RefAcctNo + '\'' +
                ", RefSeqNo1='" + RefSeqNo1 + '\'' +
                ", RefSeqNo2='" + RefSeqNo2 + '\'' +
                ", Summary='" + Summary + '\'' +
                ", Remark='" + Remark + '\'' +
                '}';
    }
}
