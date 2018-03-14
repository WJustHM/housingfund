package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 单笔转账(BDC109)输出格式
 */
public class SingleTransferAccountOut implements Serializable{
    private static final long serialVersionUID = 7146610565701169641L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;

    /**
     * 银行主机处理状态(required)
     */
    private String HostStatus;

    /**
     * 银行主机流水号
     */
    private String HostSeqNo;

    public SingleTransferAccountOut() {
    }

    public SingleTransferAccountOut(CenterHeadOut centerHeadOut, String hostStatus) {
        this.centerHeadOut = centerHeadOut;
        HostStatus = hostStatus;
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    public String getHostStatus() {
        return HostStatus;
    }

    public void setHostStatus(String hostStatus) {
        HostStatus = hostStatus;
    }

    public String getHostSeqNo() {
        return HostSeqNo;
    }

    public void setHostSeqNo(String hostSeqNo) {
        HostSeqNo = hostSeqNo;
    }

    @Override
    public String toString() {
        return "SingleTransferAccountOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", HostStatus='" + HostStatus + '\'' +
                ", HostSeqNo='" + HostSeqNo + '\'' +
                '}';
    }
}
