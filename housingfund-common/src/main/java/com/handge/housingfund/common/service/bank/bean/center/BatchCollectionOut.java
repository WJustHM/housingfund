package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 批量收款(BDC104)输出格式
 */
public class BatchCollectionOut implements Serializable {
    private static final long serialVersionUID = -2323708538796439898L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 批量编号(required)
     */
    private String BatchNo;

    public BatchCollectionOut() {
    }

    public BatchCollectionOut(CenterHeadOut centerHeadOut, String batchNo) {
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
        return "BatchCollectionOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", BatchNo='" + BatchNo + '\'' +
                '}';
    }
}
