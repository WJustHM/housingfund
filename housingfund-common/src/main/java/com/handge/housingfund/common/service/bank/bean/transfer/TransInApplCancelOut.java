package com.handge.housingfund.common.service.bank.bean.transfer;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 转入申请撤销(BDC903)输出格式
 */
public class TransInApplCancelOut implements Serializable {
    private static final long serialVersionUID = 4689544890079759404L;
    /**
     * CenterHeadOut(required), 公积金中心发起交易输出报文头
     */
    private CenterHeadOut centerHeadOut;

    public TransInApplCancelOut() {
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    @Override
    public String toString() {
        return "TranInApplCancelOut{" +
                "centerHeadOut=" + centerHeadOut +
                '}';
    }
}
