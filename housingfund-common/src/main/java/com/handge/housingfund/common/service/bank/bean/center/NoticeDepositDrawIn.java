package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 通知存款支取(BDC118)输入格式
 */
public class NoticeDepositDrawIn implements Serializable{
    private static final long serialVersionUID = 3286239859674090913L;
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
     * 支取金额(required)
     */
    private BigDecimal DrawAmt;
    /**
     * 支取方式(required)
     */
    private String DrawType;
    /**
     *册号
     */
    private String BookNo;
    /**
     *笔号
     */
    private int BookListNo;
    /**
     *通知设定序号
     */
    private String NoticeSetSerialNo;
    /**
     *通知标志
     */
    private String NoticeFlag;
    /**
     * 存入日期(required)
     */
    private String DepositBeginDate;
    /**
     *通知支取日
     */
    private String NoticeDrawDate;
    /**
     *通知设定日期
     */
    private String NoticeDrawSetDate;
    /**
     *通知支取金额
     */
    private BigDecimal NoticeDrawAmt;
    /**
     * 通知存款金额(required)
     */
    private BigDecimal NoticeDepositAmt;
    /**
     *活期存款账号
     */
    private String ActivedAcctNo;
    /**
     *活期账户户名
     */
    private String ActivedAcctName;
    /**
     * 活期账户开户机构名
     */
    private String ActivedAcctOpenBank;
    /**
     * 存期(required)
     */
    private String DepositPeriod;

    public NoticeDepositDrawIn() {
    }

    public NoticeDepositDrawIn(CenterHeadIn centerHeadIn, String currNo, String fixedAcctNo, String fixedAcctName, BigDecimal drawAmt,
                               String drawType, String depositBeginDate, BigDecimal noticeDepositAmt, String depositPeriod) {
        this.centerHeadIn = centerHeadIn;
        CurrNo = currNo;
        FixedAcctNo = fixedAcctNo;
        FixedAcctName = fixedAcctName;
        DrawAmt = drawAmt;
        DrawType = drawType;
        DepositBeginDate = depositBeginDate;
        NoticeDepositAmt = noticeDepositAmt;
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

    public BigDecimal getDrawAmt() {
        return DrawAmt;
    }

    public void setDrawAmt(BigDecimal drawAmt) {
        DrawAmt = drawAmt;
    }

    public String getDrawType() {
        return DrawType;
    }

    public void setDrawType(String drawType) {
        DrawType = drawType;
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

    public String getNoticeSetSerialNo() {
        return NoticeSetSerialNo;
    }

    public void setNoticeSetSerialNo(String noticeSetSerialNo) {
        NoticeSetSerialNo = noticeSetSerialNo;
    }

    public String getNoticeFlag() {
        return NoticeFlag;
    }

    public void setNoticeFlag(String noticeFlag) {
        NoticeFlag = noticeFlag;
    }

    public String getDepositBeginDate() {
        return DepositBeginDate;
    }

    public void setDepositBeginDate(String depositBeginDate) {
        DepositBeginDate = depositBeginDate;
    }

    public String getNoticeDrawDate() {
        return NoticeDrawDate;
    }

    public void setNoticeDrawDate(String noticeDrawDate) {
        NoticeDrawDate = noticeDrawDate;
    }

    public String getNoticeDrawSetDate() {
        return NoticeDrawSetDate;
    }

    public void setNoticeDrawSetDate(String noticeDrawSetDate) {
        NoticeDrawSetDate = noticeDrawSetDate;
    }

    public BigDecimal getNoticeDrawAmt() {
        return NoticeDrawAmt;
    }

    public void setNoticeDrawAmt(BigDecimal noticeDrawAmt) {
        NoticeDrawAmt = noticeDrawAmt;
    }

    public BigDecimal getNoticeDepositAmt() {
        return NoticeDepositAmt;
    }

    public void setNoticeDepositAmt(BigDecimal noticeDepositAmt) {
        NoticeDepositAmt = noticeDepositAmt;
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

    public String getDepositPeriod() {
        return DepositPeriod;
    }

    public void setDepositPeriod(String depositPeriod) {
        DepositPeriod = depositPeriod;
    }

    @Override
    public String toString() {
        return "NoticeDepositDrawIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", CurrNo='" + CurrNo + '\'' +
                ", CurrIden='" + CurrIden + '\'' +
                ", FixedAcctNo='" + FixedAcctNo + '\'' +
                ", FixedAcctName='" + FixedAcctName + '\'' +
                ", FixedAcctOpenBank='" + FixedAcctOpenBank + '\'' +
                ", DrawAmt=" + DrawAmt +
                ", DrawType='" + DrawType + '\'' +
                ", BookNo=" + BookNo +
                ", BookListNo=" + BookListNo +
                ", NoticeSetSerialNo='" + NoticeSetSerialNo + '\'' +
                ", NoticeFlag='" + NoticeFlag + '\'' +
                ", DepositBeginDate='" + DepositBeginDate + '\'' +
                ", CrAccNoticeDrawDatetName='" + NoticeDrawDate + '\'' +
                ", NoticeDrawSetDate='" + NoticeDrawSetDate + '\'' +
                ", NoticeDrawAmt=" + NoticeDrawAmt +
                ", NoticeDepositAmt=" + NoticeDepositAmt +
                ", ActivedAcctNo='" + ActivedAcctNo + '\'' +
                ", ActivedAcctName='" + ActivedAcctName + '\'' +
                ", ActivedAcctOpenBank='" + ActivedAcctOpenBank + '\'' +
                ", DepositPeriod='" + DepositPeriod + '\'' +
                '}';
    }
}
