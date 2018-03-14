package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 定期转存方式设定(BDC116)输出格式
 */
public class FixedTransferModeSetOut implements Serializable {
    private static final long serialVersionUID = -1680259079499444352L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;

    public FixedTransferModeSetOut() {
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    @Override
    public String toString() {
        return "FixedTransferModeSetOut{" +
                "centerHeadOut=" + centerHeadOut +
                '}';
    }
}
