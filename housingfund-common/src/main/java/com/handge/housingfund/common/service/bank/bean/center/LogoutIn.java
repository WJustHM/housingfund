package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 系统签退(BDC002)输入格式
 */
public class LogoutIn implements Serializable {
    private static final long serialVersionUID = -5878442011931859036L;
    /**
     * CenterHeadIn(reqiured)
     */
    private CenterHeadIn centerHeadIn;

    public LogoutIn() {
    }

    public LogoutIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    public CenterHeadIn getCenterHeadIn() {
        return centerHeadIn;
    }

    public void setCenterHeadIn(CenterHeadIn centerHeadIn) {
        this.centerHeadIn = centerHeadIn;
    }

    @Override
    public String toString() {
        return "LogoutIn{" +
                "centerHeadIn=" + centerHeadIn +
                '}';
    }
}
