package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 通知存款支取设定(BDC119)输入格式
 */
public class NoticeDepositDrawSetIn implements Serializable {
    private static final long serialVersionUID = -5808912257693009028L;
    /**
     * CenterHeadIn(required)
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 币种(required)
     */
    private String CurrNo ;
    /**
     * 钞汇鉴别
     */
    private String CurrIden;
    /**
     * 通知存款账号(required)
     */
    private String FixedAcctNo;
    /**
     * 通知存款户名(required)
     */
    private String FixedAcctName;
    /**
     * 通知账户开户机构名
     */
    private String FixedAcctOpenBank;
    /**
     * 册号(required)
     */
    private String BookNo;
    /**
     * 笔号(required)
     */
    private int BookListNo;
    /**
     * 支取金额(required)
     */
    private BigDecimal DrawAmt;
    /**
     * 通知设定日期(required)
     */
    private String NoticeDrawSetDate;
    /**
     * 存单余额(required)
     */
    private BigDecimal Balance;
    /**
     * 存期(required)
     */
    private String DepositPeriod;

    public NoticeDepositDrawSetIn() {
    }

    public NoticeDepositDrawSetIn(CenterHeadIn centerHeadIn, String currNo, String fixedAcctNo, String fixedAcctName,
                                  String bookNo, int bookListNo, BigDecimal drawAmt, String noticeDrawSetDate, BigDecimal balance, String depositPeriod) {
        this.centerHeadIn = centerHeadIn;
        CurrNo = currNo;
        FixedAcctNo = fixedAcctNo;
        FixedAcctName = fixedAcctName;
        BookNo = bookNo;
        BookListNo = bookListNo;
        DrawAmt = drawAmt;
        NoticeDrawSetDate = noticeDrawSetDate;
        Balance = balance;
        DepositPeriod = depositPeriod;
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

    public BigDecimal getDrawAmt() {
        return DrawAmt;
    }

    public void setDrawAmt(BigDecimal drawAmt) {
        DrawAmt = drawAmt;
    }

    public String getNoticeDrawSetDate() {
        return NoticeDrawSetDate;
    }

    public void setNoticeDrawSetDate(String noticeDrawSetDate) {
        NoticeDrawSetDate = noticeDrawSetDate;
    }

    public BigDecimal getBalance() {
        return Balance;
    }

    public void setBalance(BigDecimal balance) {
        Balance = balance;
    }

    public String getDepositPeriod() {
        return DepositPeriod;
    }

    public void setDepositPeriod(String depositPeriod) {
        DepositPeriod = depositPeriod;
    }

    @Override
    public String toString() {
        return "NoticeDepositDrawSetIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", CurrNo='" + CurrNo + '\'' +
                ", CurrIden='" + CurrIden + '\'' +
                ", FixedAcctNo='" + FixedAcctNo + '\'' +
                ", FixedAcctName='" + FixedAcctName + '\'' +
                ", FixedAcctOpenBank='" + FixedAcctOpenBank + '\'' +
                ", BookNo=" + BookNo +
                ", BookListNo=" + BookListNo +
                ", DrawAmt=" + DrawAmt +
                ", NoticeDrawSetDate='" + NoticeDrawSetDate + '\'' +
                ", Balance=" + Balance +
                ", DepositPeriod='" + DepositPeriod + '\'' +
                '}';
    }
}
