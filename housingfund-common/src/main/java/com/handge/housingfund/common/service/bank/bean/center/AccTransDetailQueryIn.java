package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 账户交易明细查询(BDC113)输入格式
 */
public class AccTransDetailQueryIn implements Serializable {
    private static final long serialVersionUID = 1170722461606505491L;
    /**
     * CenterHeadIn(required)
     */
     private CenterHeadIn centerHeadIn ;
    /**
     * 交易开始日期(required)
     */
     private String TxDateBegin ;
    /**
     * 交易结束日期(required)
     */
     private String TxDateEnd ;

    public AccTransDetailQueryIn() {
    }

    public AccTransDetailQueryIn(CenterHeadIn centerHeadIn, String txDateBegin, String txDateEnd) {
        this.centerHeadIn = centerHeadIn;
        TxDateBegin = txDateBegin;
        TxDateEnd = txDateEnd;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public String getTxDateBegin() {
        return TxDateBegin;
    }

    public void setTxDateBegin(String txDateBegin) {
        TxDateBegin = txDateBegin;
    }

    public String getTxDateEnd() {
        return TxDateEnd;
    }

    public void setTxDateEnd(String txDateEnd) {
        TxDateEnd = txDateEnd;
    }

    @Override
    public String toString() {
        return "AccTransDetailQueryIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", TxDateBegin='" + TxDateBegin + '\'' +
                ", TxDateEnd='" + TxDateEnd + '\'' +
                '}';
    }
}
