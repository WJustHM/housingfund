package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 通知存款支取通知取消(BDC120)输出格式
 */
public class NoticeDepositDrawCancelOut implements Serializable{
    private static final long serialVersionUID = -8796742661594555989L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;

    public NoticeDepositDrawCancelOut() {
    }

    public NoticeDepositDrawCancelOut(CenterHeadOut centerHeadOut) {
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
        return "NoticeDepositDrawCancelOut{" +
                "centerHeadOut=" + centerHeadOut +
                '}';
    }
}
