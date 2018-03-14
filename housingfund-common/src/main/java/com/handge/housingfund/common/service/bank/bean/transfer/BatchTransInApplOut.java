package com.handge.housingfund.common.service.bank.bean.transfer;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 批量转入申请(BDC902)输出格式
 */
public class BatchTransInApplOut  implements Serializable {
    private static final long serialVersionUID = -4286412788924260529L;
    /**
     * CenterHeadOut(required), 公积金中心发起交易输出报文头
     */
    private CenterHeadOut centerHeadOut;

    public BatchTransInApplOut() {
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    @Override
    public String toString() {
        return "BatchTranInApplOut{" +
                "centerHeadOut=" + centerHeadOut +
                '}';
    }
}
