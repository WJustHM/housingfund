package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 批量业务结果查询(BDC107)输入格式
 */
public class BatchResultQueryIn implements Serializable {
    private static final long serialVersionUID = 8883429274571901136L;
    /**
     * CenterHeadIn(required)
     */
    private CenterHeadIn centerHeadIn;
    /**
     * 批量编号(required)
     */
    private String BatchNo;

    public BatchResultQueryIn() {
    }

    public BatchResultQueryIn(CenterHeadIn centerHeadIn, String batchNo) {
        this.centerHeadIn = centerHeadIn;
        BatchNo = batchNo;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    @Override
    public String toString() {
        return "BatchResultQueryIn{" +
                "centerHeadIn=" + centerHeadIn +
                ", BatchNo='" + BatchNo + '\'' +
                '}';
    }
}
