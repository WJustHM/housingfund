package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 通知存款支取设定(BDC119)输出格式
 */
public class NoticeDepositDrawSetOut implements Serializable {
    private static final long serialVersionUID = -1134317990761389975L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;

    public NoticeDepositDrawSetOut() {
    }

    public NoticeDepositDrawSetOut(CenterHeadOut centerHeadOut) {
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
        return "NoticeDepositDrawSetOut{" +
                "centerHeadOut=" + centerHeadOut +
                '}';
    }
}
