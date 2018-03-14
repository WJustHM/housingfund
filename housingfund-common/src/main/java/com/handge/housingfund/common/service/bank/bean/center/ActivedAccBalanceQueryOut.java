package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 活期账户实时余额查询(BDC123)输出格式
 */
public class ActivedAccBalanceQueryOut implements Serializable {
    private static final long serialVersionUID = -3475737096214856287L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 账号(required)
     */
    private String AcctNo;
    /**
     * 账户户名(required)
     */
    private String AcctName;
    /**
     * 账户余额(required)
     */
    private BigDecimal AcctBal;
    /**
     * 账户可用额度(required)
     */
    private BigDecimal AcctRestBal;
    /**
     * 账户透支额(required)
     */
    private BigDecimal AcctOverBal;
    /**
     * 账户状态(required)
     */
    private String AcctStatus;

    public ActivedAccBalanceQueryOut() {
    }

    public ActivedAccBalanceQueryOut(CenterHeadOut centerHeadOut, String acctNo, String acctName, BigDecimal acctBal, BigDecimal acctRestBal, BigDecimal acctOverBal, String acctStatus) {
        this.centerHeadOut = centerHeadOut;
        AcctNo = acctNo;
        AcctName = acctName;
        AcctBal = acctBal;
        AcctRestBal = acctRestBal;
        AcctOverBal = acctOverBal;
        AcctStatus = acctStatus;
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    public String getAcctNo() {
        return AcctNo;
    }

    public void setAcctNo(String acctNo) {
        AcctNo = acctNo;
    }

    public String getAcctName() {
        return AcctName;
    }

    public void setAcctName(String acctName) {
        AcctName = acctName;
    }

    public BigDecimal getAcctBal() {
        return AcctBal;
    }

    public void setAcctBal(BigDecimal acctBal) {
        AcctBal = acctBal;
    }

    public BigDecimal getAcctRestBal() {
        return AcctRestBal;
    }

    public void setAcctRestBal(BigDecimal acctRestBal) {
        AcctRestBal = acctRestBal;
    }

    public BigDecimal getAcctOverBal() {
        return AcctOverBal;
    }

    public void setAcctOverBal(BigDecimal acctOverBal) {
        AcctOverBal = acctOverBal;
    }

    public String getAcctStatus() {
        return AcctStatus;
    }

    public void setAcctStatus(String acctStatus) {
        AcctStatus = acctStatus;
    }

    @Override
    public String toString() {
        return "ActivedAccBalanceQueryOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", AcctNo='" + AcctNo + '\'' +
                ", AcctName='" + AcctName + '\'' +
                ", AcctBal=" + AcctBal +
                ", AcctRestBal=" + AcctRestBal +
                ", AcctOverBal=" + AcctOverBal +
                ", AcctStatus='" + AcctStatus + '\'' +
                '}';
    }
}
