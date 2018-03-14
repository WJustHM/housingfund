package com.handge.housingfund.common.service.bank.bean.transfer;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 单笔转出信息(BDC905)输出格式
 */
public class SingleTransOutInfoOut implements Serializable {
    private static final long serialVersionUID = 2581695649527605750L;
    /**
     * CenterHeadOut(required), 公积金中心发起交易输出报文头
     */
    private CenterHeadOut centerHeadOut;

    public SingleTransOutInfoOut() {
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    @Override
    public String toString() {
        return "SingleTranOutApplOut{" +
                "centerHeadOut=" + centerHeadOut +
                '}';
    }
}
