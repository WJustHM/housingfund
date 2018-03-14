package com.handge.housingfund.common.service.bank.bean.transfer;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 批量转出信息(BDC906)输出格式
 */
public class BatchTransOutInfoOut implements Serializable {
    private static final long serialVersionUID = 7640629143628694784L;
    /**
     * CenterHeadOut(required), 公积金中心发起交易输出报文头
     */
    private CenterHeadOut centerHeadOut;

    public BatchTransOutInfoOut() {
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    @Override
    public String toString() {
        return "BatchTranOutInfoOut{" +
                "centerHeadOut=" + centerHeadOut +
                '}';
    }
}
