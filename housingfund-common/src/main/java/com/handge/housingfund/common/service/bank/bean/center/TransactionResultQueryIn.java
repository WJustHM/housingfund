package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 交易结果查询(BDC110)输入格式
 */
public class TransactionResultQueryIn implements Serializable{
    private static final long serialVersionUID = -479345449593178852L;
    /**
     * CenterHeadIn(required)
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 原交易日期(required)
     */
    private String TxDate;
    /**
     * 原交易流水号(required)
     */
    private String TxSeqNo;

    public TransactionResultQueryIn() {
    }

    public TransactionResultQueryIn(CenterHeadIn centerHeadIn, String txDate, String txSeqNo) {
        this.centerHeadIn = centerHeadIn;
        TxDate = txDate;
        TxSeqNo = txSeqNo;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public String getTxDate() {
        return TxDate;
    }

    public void setTxDate(String txDate) {
        TxDate = txDate;
    }

    public String getTxSeqNo() {
        return TxSeqNo;
    }

    public void setTxSeqNo(String txSeqNo) {
        TxSeqNo = txSeqNo;
    }

    @Override
    public String toString() {
        return "TransactionResultQueryIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", TxDate='" + TxDate + '\'' +
                ", TxSeqNo='" + TxSeqNo + '\'' +
                '}';
    }
}
