package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 活期转通知存款(BDC117)输入格式
 */
public class Actived2NoticeDepositIn implements Serializable {
    private static final long serialVersionUID = 2734277798913652051L;
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
     * 通知存款账号
     */
    private String FixedAcctNo;
    /**
     * 通知账户户名
     */
    private String FixedAcctName;
    /**
     * 通知存款金额(required)
     */
    private BigDecimal NoticeDepositAmt;
    /**
     * 活期存款账号(required)
     */
    private String ActivedAcctNo;
    /**
     * 活期账户户名(required)
     */
    private String ActivedAcctName;
    /**
     * 存期(required)
     */
    private String DepositPeriod;

    public Actived2NoticeDepositIn() {
    }

    public Actived2NoticeDepositIn(CenterHeadIn centerHeadIn, String CurrNo, BigDecimal NoticeDepositAmt, String ActivedAcctNo, String ActivedAcctName, String DepositPeriod){
        this.centerHeadIn = centerHeadIn;
        this.CurrNo = CurrNo;
        this.NoticeDepositAmt = NoticeDepositAmt;
        this.ActivedAcctNo = ActivedAcctNo;
        this.ActivedAcctName = ActivedAcctName;
        this.DepositPeriod = DepositPeriod;
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

    public String getDepositPeriod() {
        return DepositPeriod;
    }

    public void setDepositPeriod(String depositPeriod) {
        DepositPeriod = depositPeriod;
    }

    @Override
    public String toString() {
        return "Actived2NoticeDepositIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", CurrNo='" + CurrNo + '\'' +
                ", CurrIden='" + CurrIden + '\'' +
                ", FixedAcctNo='" + FixedAcctNo + '\'' +
                ", FixedAcctName='" + FixedAcctName + '\'' +
                ", NoticeDepositAmt=" + NoticeDepositAmt +
                ", ActivedAcctNo='" + ActivedAcctNo + '\'' +
                ", ActivedAcctName='" + ActivedAcctName + '\'' +
                ", DepositPeriod='" + DepositPeriod + '\'' +
                '}';
    }
}
