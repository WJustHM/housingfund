package com.handge.housingfund.common.service.bank.bean.center;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 定期账户余额查询(BDC122)结果列表
 */
public class BDC122Summary implements Serializable {
    private static final long serialVersionUID = -4300265022637836643L;
    /**
     * 存折册号
     */
    private String BookNo;
    /**
     * 本册笔号
     */
    private int BookListNo;
    /**
     * 币种(required)
     */
    private String CurrNo; 
    /**
     * 存期(required)
     */
    private BigDecimal DepositPeriod;
    /**
     * 到期日期(required)
     */
    private String DepositEndDate; 
    /**
     * 开户日期(required)
     */
    private String BegDate; 
    /**
     * 开户金额
     */
    private BigDecimal BegAmt;
    /**
     * 实际金额(required)
     */
    private BigDecimal DrawAmt;
    /**
     * 结清利息
     */
    private BigDecimal Interest;
    /**
     * 冻结状态(required)
     */
    private String FreezeType; 
    /**
     * 挂失标志
     */
    private String LossFlag; 
    /**
     * 账户状态(required)
     */
    private String AcctStatus; 
    /**
     * 钞汇鉴别(required)
     */
    private String CurrIden; 
    /**
     * 利率(required)
     */
    private BigDecimal InterestRate;

    public BDC122Summary() {
    }

    public String getBookNo() {
        return BookNo;
    }

    public void setBookNo(String bookNo) {
        BookNo = bookNo;
    }

    public int getBookListNo() {
        return BookListNo;
    }

    public void setBookListNo(int bookListNo) {
        BookListNo = bookListNo;
    }

    public String getCurrNo() {
        return CurrNo;
    }

    public void setCurrNo(String currNo) {
        CurrNo = currNo;
    }

    public BigDecimal getDepositPeriod() {
        return DepositPeriod;
    }

    public void setDepositPeriod(BigDecimal depositPeriod) {
        DepositPeriod = depositPeriod;
    }

    public String getDepositEndDate() {
        return DepositEndDate;
    }

    public void setDepositEndDate(String depositEndDate) {
        DepositEndDate = depositEndDate;
    }

    public String getBegDate() {
        return BegDate;
    }

    public void setBegDate(String begDate) {
        BegDate = begDate;
    }

    public BigDecimal getBegAmt() {
        return BegAmt;
    }

    public void setBegAmt(BigDecimal begAmt) {
        BegAmt = begAmt;
    }

    public BigDecimal getDrawAmt() {
        return DrawAmt;
    }

    public void setDrawAmt(BigDecimal drawAmt) {
        DrawAmt = drawAmt;
    }

    public BigDecimal getInterest() {
        return Interest;
    }

    public void setInterest(BigDecimal interest) {
        Interest = interest;
    }

    public String getFreezeType() {
        return FreezeType;
    }

    public void setFreezeType(String freezeType) {
        FreezeType = freezeType;
    }

    public String getLossFlag() {
        return LossFlag;
    }

    public void setLossFlag(String lossFlag) {
        LossFlag = lossFlag;
    }

    public String getAcctStatus() {
        return AcctStatus;
    }

    public void setAcctStatus(String acctStatus) {
        AcctStatus = acctStatus;
    }

    public String getCurrIden() {
        return CurrIden;
    }

    public void setCurrIden(String currIden) {
        CurrIden = currIden;
    }

    public BigDecimal getInterestRate() {
        return InterestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        InterestRate = interestRate;
    }

    @Override
    public String toString() {
        return "BDC112Summary{" +
                "BookNo=" + BookNo +
                ", BookListNo=" + BookListNo +
                ", CurrNo='" + CurrNo + '\'' +
                ", DepositPeriod=" + DepositPeriod +
                ", DepositEndDate='" + DepositEndDate + '\'' +
                ", BegDate='" + BegDate + '\'' +
                ", BegAmt=" + BegAmt +
                ", DrawAmt=" + DrawAmt +
                ", Interest=" + Interest +
                ", FreezeType='" + FreezeType + '\'' +
                ", LossFlag='" + LossFlag + '\'' +
                ", AcctStatus='" + AcctStatus + '\'' +
                ", CurrIden='" + CurrIden + '\'' +
                ", InterestRate=" + InterestRate +
                '}';
    }
}
