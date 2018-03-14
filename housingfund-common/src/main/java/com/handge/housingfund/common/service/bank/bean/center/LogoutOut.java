package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 *系统签退(BDC002)输出格式
 */
public class LogoutOut implements Serializable {
    private static final long serialVersionUID = -9059133080192815541L;
    /**
     * CenterHeadOut(reqiured)
     */
    private CenterHeadOut centerHeadOut;

    public LogoutOut() {
    }

    public LogoutOut(CenterHeadOut centerHeadOut) {
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
        return "LogoutOut{" +
                "centerHeadOut=" + centerHeadOut +
                '}';
    }
}
