package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *单笔转账(BDC109)输入格式
 */
public class SingleTransferAccountIn implements Serializable{
    private static final long serialVersionUID = 6448895559522379009L;
    /**
     * CenterHeadIn(required)
     */
    private CenterHeadIn centerHeadIn; //Head
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
     * 业务明细流水号
     */
    private String RefSeqNo ;
    /**
     * 摘要(required)
     */
    private String Summary;
    /**
     * 备注(required)
     */
    private String Remark;

    public SingleTransferAccountIn() {
    }

    public SingleTransferAccountIn(CenterHeadIn centerHeadIn, String settleType, String busType, String deAcctNo,
                                   String deAcctName, String deAcctClass, String crAcctNo, String crAcctName,
                                   String crAcctClass, String crBankClass, BigDecimal amt, String summary, String remark) {
        this.centerHeadIn = centerHeadIn;
        SettleType = settleType;
        BusType = busType;
        DeAcctNo = deAcctNo;
        DeAcctName = deAcctName;
        DeAcctClass = deAcctClass;
        CrAcctNo = crAcctNo;
        CrAcctName = crAcctName;
        CrAcctClass = crAcctClass;
        CrBankClass = crBankClass;
        Amt = amt;
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

    public String getRefSeqNo() {
        return RefSeqNo;
    }

    public void setRefSeqNo(String refSeqNo) {
        RefSeqNo = refSeqNo;
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
        return "SingleTransferAccountIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", SettleType='" + SettleType + '\'' +
                ", BusType='" + BusType + '\'' +
                ", DeAcctNo='" + DeAcctNo + '\'' +
                ", DeAcctName='" + DeAcctName + '\'' +
                ", DeAcctClass='" + DeAcctClass + '\'' +
                ", CrAcctNo='" + CrAcctNo + '\'' +
                ", CrAcctName='" + CrAcctName + '\'' +
                ", CrAcctClass='" + CrAcctClass + '\'' +
                ", CrBankClass='" + CrBankClass + '\'' +
                ", CrBankName='" + CrBankName + '\'' +
                ", CrChgNo='" + CrChgNo + '\'' +
                ", Amt=" + Amt +
                ", RefAcctNo='" + RefAcctNo + '\'' +
                ", RefSeqNo='" + RefSeqNo + '\'' +
                ", Summary='" + Summary + '\'' +
                ", Remark='" + Remark + '\'' +
                '}';
    }
}
