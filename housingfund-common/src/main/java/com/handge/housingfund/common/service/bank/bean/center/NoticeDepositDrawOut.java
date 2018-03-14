package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 通知存款支取(BDC118)输格式
 */
public class NoticeDepositDrawOut implements Serializable{
    private static final long serialVersionUID = -3025453885689924161L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut; //Head
    /**
     * 银行主机流水号(required)
     */
    private String HostSeqNo;

    public NoticeDepositDrawOut() {
    }

    public NoticeDepositDrawOut(CenterHeadOut centerHeadOut, String hostSeqNo) {
        this.centerHeadOut = centerHeadOut;
        HostSeqNo = hostSeqNo;
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    public String getHostSeqNo() {
        return HostSeqNo;
    }

    public void setHostSeqNo(String hostSeqNo) {
        HostSeqNo = hostSeqNo;
    }

    @Override
    public String toString() {
        return "NoticeDepositDrawOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", HostSeqNo='" + HostSeqNo + '\'' +
                '}';
    }
}
