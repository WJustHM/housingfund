package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 系统签到(BDC001)输入格式
 */
public class LoginOut implements Serializable {
    private static final long serialVersionUID = -5115364864885400731L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut; //Head
    /**
     * 会话密钥密文(required)
     */
    private String HandKey;

    public LoginOut() {
    }

    public LoginOut(CenterHeadOut centerHeadOut, String handKey) {
        this.centerHeadOut = centerHeadOut;
        HandKey = handKey;
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    public String getHandKey() {
        return HandKey;
    }

    public void setHandKey(String handKey) {
        HandKey = handKey;
    }

    @Override
    public String toString() {
        return "LoginOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", HandKey='" + HandKey + '\'' +
                '}';
    }
}
