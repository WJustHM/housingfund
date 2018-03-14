package com.handge.housingfund.common.service.bank.bean.transfer;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 单笔转入申请(BDC901)输出格式
 */
public class SingleTransInApplOut implements Serializable {
    private static final long serialVersionUID = -1556216414876283542L;
    /**
     * CenterHeadOut(required), 公积金中心发起交易输出报文头
     */
    private CenterHeadOut centerHeadOut;

    public SingleTransInApplOut() {
    }

    public SingleTransInApplOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    @Override
    public String toString() {
        return "SingleTransInApplOut{" +
                "centerHeadOut=" + centerHeadOut +
                '}';
    }
}
