package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 单笔收款 (BDC102)输出格式
 */
public class SingleCollectionOut implements Serializable{
    private static final long serialVersionUID = -6002130020796730230L;
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

    public SingleCollectionOut() {
    }

    public SingleCollectionOut(CenterHeadOut centerHeadOut, String hostStatus) {
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
        return "SingleCollectionOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", HostStatus='" + HostStatus + '\'' +
                ", HostSeqNo='" + HostSeqNo + '\'' +
                '}';
    }
}
