package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 通知存款支取通知取消(BDC120)输入格式
 */
public class NoticeDepositDrawCancelIn implements Serializable {
    private static final long serialVersionUID = -2387884990148574726L;
    /**
     * CenterHeadIn(required)
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 币种(required)
     */
    private String CurrNo;
    /**
     *钞汇鉴别
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
     *通知账户开户机构名
     */
    private String FixedAcctOpenBank;
    /**
     * 通知设定序号(required)
     */
    private String NoticeSetSerialNo;
    /**
     *册号
     */
    private String BookNo;
    /**
     *笔号
     */
    private int BookListNo;
    /**
     * 通知标志(required)
     */
    private String NoticeFlag;
    /**
     * 通知设定日期(required)
     */
    private String NoticeDrawSetDate;
    /**
     *  通知支取日期(required)
     */
    private String NoticeDrawDate;
    /**
     *  支取金额(required)
     */
    private BigDecimal DrawAmt;
    /**
     * 存单余额(required)
     */
    private BigDecimal Balance;
    /**
     * 存期(required)
     */
    private String DepositPeriod;

    public NoticeDepositDrawCancelIn() {
    }

    public NoticeDepositDrawCancelIn(CenterHeadIn centerHeadIn, String currNo, String fixedAcctNo, String fixedAcctName,
                                     String noticeSetSerialNo, String noticeFlag, String noticeDrawSetDate,
                                     String noticeDrawDate, BigDecimal drawAmt, BigDecimal balance, String depositPeriod) {
        this.centerHeadIn = centerHeadIn;
        CurrNo = currNo;
        FixedAcctNo = fixedAcctNo;
        FixedAcctName = fixedAcctName;
        NoticeSetSerialNo = noticeSetSerialNo;
        NoticeFlag = noticeFlag;
        NoticeDrawSetDate = noticeDrawSetDate;
        NoticeDrawDate = noticeDrawDate;
        DrawAmt = drawAmt;
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

    public String getNoticeSetSerialNo() {
        return NoticeSetSerialNo;
    }

    public void setNoticeSetSerialNo(String noticeSetSerialNo) {
        NoticeSetSerialNo = noticeSetSerialNo;
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

    public String getNoticeFlag() {
        return NoticeFlag;
    }

    public void setNoticeFlag(String noticeFlag) {
        NoticeFlag = noticeFlag;
    }

    public String getNoticeDrawSetDate() {
        return NoticeDrawSetDate;
    }

    public void setNoticeDrawSetDate(String noticeDrawSetDate) {
        NoticeDrawSetDate = noticeDrawSetDate;
    }

    public String getNoticeDrawDate() {
        return NoticeDrawDate;
    }

    public void setNoticeDrawDate(String noticeDrawDate) {
        NoticeDrawDate = noticeDrawDate;
    }

    public BigDecimal getDrawAmt() {
        return DrawAmt;
    }

    public void setDrawAmt(BigDecimal drawAmt) {
        DrawAmt = drawAmt;
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
        return "NoticeDepositDrawCancelIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", CurrNo='" + CurrNo + '\'' +
                ", CurrIden='" + CurrIden + '\'' +
                ", FixedAcctNo='" + FixedAcctNo + '\'' +
                ", FixedAcctName='" + FixedAcctName + '\'' +
                ", FixedAcctOpenBank='" + FixedAcctOpenBank + '\'' +
                ", NoticeSetSerialNo='" + NoticeSetSerialNo + '\'' +
                ", BookNo=" + BookNo +
                ", BookListNo=" + BookListNo +
                ", NoticeFlag='" + NoticeFlag + '\'' +
                ", NoticeDrawSetDate='" + NoticeDrawSetDate + '\'' +
                ", NoticeDrawDate='" + NoticeDrawDate + '\'' +
                ", DrawAmt=" + DrawAmt +
                ", Balance=" + Balance +
                ", DepositPeriod='" + DepositPeriod + '\'' +
                '}';
    }
}
