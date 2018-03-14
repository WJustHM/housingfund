package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 定期支取(BDC115)输入格式
 */
public class FixedDrawIn implements Serializable {
    private static final long serialVersionUID = 879624633013800023L;
    /**
     * CenterHeadIn(required)
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 币种(required)
     */
    private String CurrNo;
    /**
     * 钞汇鉴别
     */
    private String CurrIden;
    /**
     * 定期账号(required)
     */
    private String FixedAcctNo;
    /**
     * 定期户名(required)
     */
    private String FixedAcctName;
    /**
     * 定期账户开户机构名
     */
    private String FixedAcctOpenBank;
    /**
     * 册号
     */
    private String BookNo;
    /**
     * 笔号
     */
    private int BookListNo;
    /**
     * 存入日期(required)
     */
    private String DepositBeginDate;
    /**
     * 到期日期(required)
     */
    private String DepositEndDate;
    /**
     * 活期账户
     */
    private String ActivedAcctNo;
    /**
     * 活期户名
     */
    private String ActivedAcctName;
    /**
     * 活期账户开户机构名
     */
    private String ActivedAcctOpenBank;
    /**
     * 实际金额
     */
    private BigDecimal DepostiAmt;
    /**
     * 支取金额(required)
     */
    private BigDecimal DrawAmt;
    /**
     * 存期
     */
    private String DepositPeriod;
    /**
     * 利率
     */
    private BigDecimal InterestRate;

    public FixedDrawIn() {
    }

    public FixedDrawIn(CenterHeadIn centerHeadIn, String currNo, String fixedAcctNo, String fixedAcctName, String depositBeginDate, String depositEndDate, BigDecimal drawAmt) {
        this.centerHeadIn = centerHeadIn;
        CurrNo = currNo;
        FixedAcctNo = fixedAcctNo;
        FixedAcctName = fixedAcctName;
        DepositBeginDate = depositBeginDate;
        DepositEndDate = depositEndDate;
        DrawAmt = drawAmt;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public String getCurrNo() {
        return CurrNo;
    }

    public void setCurrNo(String currNo) {
        CurrNo = currNo;
    }

    public String getCurrIden() {
        return CurrIden;
    }

    public void setCurrIden(String currIden) {
        CurrIden = currIden;
    }

    public String getFixedAcctNo() {
        return FixedAcctNo;
    }

    public void setFixedAcctNo(String fixedAcctNo) {
        FixedAcctNo = fixedAcctNo;
    }

    public String getFixedAcctName() {
        return FixedAcctName;
    }

    public void setFixedAcctName(String fixedAcctName) {
        FixedAcctName = fixedAcctName;
    }

    public String getFixedAcctOpenBank() {
        return FixedAcctOpenBank;
    }

    public void setFixedAcctOpenBank(String fixedAcctOpenBank) {
        FixedAcctOpenBank = fixedAcctOpenBank;
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

    public String getDepositBeginDate() {
        return DepositBeginDate;
    }

    public void setDepositBeginDate(String depositBeginDate) {
        DepositBeginDate = depositBeginDate;
    }

    public String getDepositEndDate() {
        return DepositEndDate;
    }

    public void setDepositEndDate(String depositEndDate) {
        DepositEndDate = depositEndDate;
    }

    public String getActivedAcctNo() {
        return ActivedAcctNo;
    }

    public void setActivedAcctNo(String activedAcctNo) {
        ActivedAcctNo = activedAcctNo;
    }

    public String getActivedAcctName() {
        return ActivedAcctName;
    }

    public void setActivedAcctName(String activedAcctName) {
        ActivedAcctName = activedAcctName;
    }

    public String getActivedAcctOpenBank() {
        return ActivedAcctOpenBank;
    }

    public void setActivedAcctOpenBank(String activedAcctOpenBank) {
        ActivedAcctOpenBank = activedAcctOpenBank;
    }

    public BigDecimal getDepostiAmt() {
        return DepostiAmt;
    }

    public void setDepostiAmt(BigDecimal depostiAmt) {
        DepostiAmt = depostiAmt;
    }

    public BigDecimal getDrawAmt() {
        return DrawAmt;
    }

    public void setDrawAmt(BigDecimal drawAmt) {
        DrawAmt = drawAmt;
    }

    public String getDepositPeriod() {
        return DepositPeriod;
    }

    public void setDepositPeriod(String depositPeriod) {
        DepositPeriod = depositPeriod;
    }

    public BigDecimal getInterestRate() {
        return InterestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        InterestRate = interestRate;
    }

    @Override
    public String toString() {
        return "FixedDrawIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", CurrNo='" + CurrNo + '\'' +
                ", CurrIden='" + CurrIden + '\'' +
                ", FixedAcctNo='" + FixedAcctNo + '\'' +
                ", FixedAcctName='" + FixedAcctName + '\'' +
                ", FixedAcctOpenBank='" + FixedAcctOpenBank + '\'' +
                ", BookNo=" + BookNo +
                ", BookListNo=" + BookListNo +
                ", DepositBeginDate='" + DepositBeginDate + '\'' +
                ", DepositEndDate='" + DepositEndDate + '\'' +
                ", ActivedAcctNo='" + ActivedAcctNo + '\'' +
                ", ActivedAcctName='" + ActivedAcctName + '\'' +
                ", ActivedAcctOpenBank='" + ActivedAcctOpenBank + '\'' +
                ", DepostiAmt=" + DepostiAmt +
                ", DrawAmt=" + DrawAmt +
                ", DepositPeriod='" + DepositPeriod + '\'' +
                ", InterestRate=" + InterestRate +
                '}';
    }
}
