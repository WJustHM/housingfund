package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 单笔收款 (BDC102)输入格式
 */
public class SingleCollectionIn implements Serializable {
    private static final long serialVersionUID = 5160628526120035561L;
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
     * 付方行名
     */
    private String DeBankName;
    /**
     * 付方联行号
     */
    private String DeChgNo;
    /**
     * 付方账户行别(required)
     */
    private String DeBankClass;
    /**
     * 多方协议号
     */
    private String ConAgrNo;
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
    private String RefSeqNo;
    /**
     * 摘要(required)
     */
    private String Summary ;
    /**
     * 备注(required)
     */
    private String Remark;

    public SingleCollectionIn() {
    }

    public SingleCollectionIn(CenterHeadIn centerHeadIn, String settleType, String busType, String crAcctNo, String crAcctName,
                              String crAcctClass, String deAcctNo, String deAcctName, String deAcctClass, String deBankClass,
                              BigDecimal amt, String summary, String remark) {
        this.centerHeadIn = centerHeadIn;
        SettleType = settleType;
        BusType = busType;
        CrAcctNo = crAcctNo;
        CrAcctName = crAcctName;
        CrAcctClass = crAcctClass;
        DeAcctNo = deAcctNo;
        DeAcctName = deAcctName;
        DeAcctClass = deAcctClass;
        DeBankClass = deBankClass;
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

    public String getDeBankName() {
        return DeBankName;
    }

    public void setDeBankName(String deBankName) {
        DeBankName = deBankName;
    }

    public String getDeChgNo() {
        return DeChgNo;
    }

    public void setDeChgNo(String deChgNo) {
        DeChgNo = deChgNo;
    }

    public String getDeBankClass() {
        return DeBankClass;
    }

    public void setDeBankClass(String deBankClass) {
        DeBankClass = deBankClass;
    }

    public String getConAgrNo() {
        return ConAgrNo;
    }

    public void setConAgrNo(String conAgrNo) {
        ConAgrNo = conAgrNo;
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
        return "SingleCollectionIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", SettleType='" + SettleType + '\'' +
                ", BusType='" + BusType + '\'' +
                ", CrAcctNo='" + CrAcctNo + '\'' +
                ", CrAcctName='" + CrAcctName + '\'' +
                ", CrAcctClass='" + CrAcctClass + '\'' +
                ", DeAcctNo='" + DeAcctNo + '\'' +
                ", DeAcctName='" + DeAcctName + '\'' +
                ", DeAcctClass='" + DeAcctClass + '\'' +
                ", DeBankName='" + DeBankName + '\'' +
                ", DeChgNo='" + DeChgNo + '\'' +
                ", DeBankClass='" + DeBankClass + '\'' +
                ", ConAgrNo='" + ConAgrNo + '\'' +
                ", Amt=" + Amt +
                ", RefAcctNo='" + RefAcctNo + '\'' +
                ", RefSeqNo='" + RefSeqNo + '\'' +
                ", Summary='" + Summary + '\'' +
                ", Remark='" + Remark + '\'' +
                '}';
    }
}
