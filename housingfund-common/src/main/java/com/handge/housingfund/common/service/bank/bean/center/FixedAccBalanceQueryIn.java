package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 定期账户余额查询(BDC122)输入格式
 */
public class FixedAccBalanceQueryIn implements Serializable {
    private static final long serialVersionUID = 2561812164326050589L;
    /**
     * CenterHeadIn(required)
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 账号(required)
     */
    private String AcctNo;
    /**
     * 定期类型(required)
     */
    private String FixedType;

    public FixedAccBalanceQueryIn() {
    }

    public FixedAccBalanceQueryIn(CenterHeadIn centerHeadIn, String acctNo, String fixedType) {
        this.centerHeadIn = centerHeadIn;
        AcctNo = acctNo;
        FixedType = fixedType;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public String getAcctNo() {
        return AcctNo;
    }

    public void setAcctNo(String acctNo) {
        AcctNo = acctNo;
    }

    public String getFixedType() {
        return FixedType;
    }

    public void setFixedType(String fixedType) {
        FixedType = fixedType;
    }

    @Override
    public String toString() {
        return "FixedAccBalanceQueryIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", AcctNo='" + AcctNo + '\'' +
                ", FixedType='" + FixedType + '\'' +
                '}';
    }
}
