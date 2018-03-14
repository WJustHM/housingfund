package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 批量付款(BDC103)输出格式
 */
public class BatchPaymentOut implements Serializable {
    private static final long serialVersionUID = -1945808737892564446L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 批量编号(required)
     */
    private String BatchNo;

    public BatchPaymentOut() {
    }

    public BatchPaymentOut(CenterHeadOut centerHeadOut, String batchNo) {
        this.centerHeadOut = centerHeadOut;
        BatchNo = batchNo;
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    @Override
    public String toString() {
        return "BatchPaymentOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", BatchNo='" + BatchNo + '\'' +
                '}';
    }
}
