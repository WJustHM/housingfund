package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;

import java.io.Serializable;

/**
 * 系统签到(BDC001)输入格式
 */
public class LoginIn implements Serializable {
    private static final long serialVersionUID = -251749835891121127L;
    /**
     * CenterHeadIn(required)
     */
    private CenterHeadIn centerHeadIn;

    public LoginIn() {
    }

    public LoginIn(CenterHeadIn centerHeadIn) {
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
        return "LoginIn{" +
                "centerHeadIn=" + centerHeadIn +
                '}';
    }
}
