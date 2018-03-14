package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;
import java.util.List;

/**
 * 通知存款支取通知查询(BDC121)输出格式
 */
public class NoticeDepositDrawQueryOut implements Serializable{
    private static final long serialVersionUID = -8693742989638325113L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 结果列表(required)
     */
    private List<BDC121Summary> SUMMARY;

    public NoticeDepositDrawQueryOut() {
    }

    public NoticeDepositDrawQueryOut(CenterHeadOut centerHeadOut, List<BDC121Summary> SUMMARY) {
        this.centerHeadOut = centerHeadOut;
        this.SUMMARY = SUMMARY;
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    public List<BDC121Summary> getSUMMARY() {
        return SUMMARY;
    }

    public void setSUMMARY(List<BDC121Summary> SUMMARY) {
        this.SUMMARY = SUMMARY;
    }

    @Override
    public String toString() {
        return "NoticeDepositDrawQueryOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", SUMMARY=" + SUMMARY +
                '}';
    }
}
