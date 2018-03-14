package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 批量业务结果下载(BDC108)输入格式
 */
public class BatchResultDownloadIn implements Serializable {
    private static final long serialVersionUID = 4051001213616946960L;
    /**
     * CenterHeadIn(required)
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 类型(required)
     */
    private String TxCodeSub;
    /**
     * 批量编号(required)
     */
    private String BatchNo;

    public BatchResultDownloadIn() {
    }

    public BatchResultDownloadIn(CenterHeadIn centerHeadIn, String txCodeSub, String batchNo) {
        this.centerHeadIn = centerHeadIn;
        TxCodeSub = txCodeSub;
        BatchNo = batchNo;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public String getTxCodeSub() {
        return TxCodeSub;
    }

    public void setTxCodeSub(String txCodeSub) {
        TxCodeSub = txCodeSub;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    @Override
    public String toString() {
        return "BatchResultDownloadIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", TxCodeSub='" + TxCodeSub + '\'' +
                ", BatchNo='" + BatchNo + '\'' +
                '}';
    }
}
