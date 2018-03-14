package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 活期账户实时余额查询(BDC123)输入格式
 */
public class ActivedAccBalanceQueryIn implements Serializable {
    private static final long serialVersionUID = 8463155784046181717L;
    /**
     * CenterHeadIn(required)
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 币种(required)
     */
    private String CurrNo;
    /**
     * 钞汇鉴别(required)
     */
    private String currIden;
    /**
     * 账号(required)
     */
    private String AcctNo;
    /**
     * 账号类型(required)
     */
    private String AcctType;

    public ActivedAccBalanceQueryIn() {
    }

    public ActivedAccBalanceQueryIn(CenterHeadIn centerHeadIn, String currNo, String currIden, String acctNo, String acctType) {
        this.centerHeadIn = centerHeadIn;
        CurrNo = currNo;
        this.currIden = currIden;
        AcctNo = acctNo;
        AcctType = acctType;
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
        return currIden;
    }

    public void setCurrIden(String currIden) {
        this.currIden = currIden;
    }

    public String getAcctNo() {
        return AcctNo;
    }

    public void setAcctNo(String acctNo) {
        AcctNo = acctNo;
    }

    public String getAcctType() {
        return AcctType;
    }

    public void setAcctType(String acctType) {
        AcctType = acctType;
    }

    @Override
    public String toString() {
        return "ActivedAccBalanceQueryIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", CurrNo='" + CurrNo + '\'' +
                ", currIden='" + currIden + '\'' +
                ", AcctNo='" + AcctNo + '\'' +
                ", AcctType='" + AcctType + '\'' +
                '}';
    }
}
