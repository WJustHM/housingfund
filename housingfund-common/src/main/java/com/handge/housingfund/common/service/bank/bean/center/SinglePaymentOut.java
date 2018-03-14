package com.handge.housingfund.common.service.bank.bean.center;


import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 单笔付款(BDC101)输出格式
 */
public class SinglePaymentOut implements Serializable{
    private static final long serialVersionUID = 6521755419355348411L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 银行主机处理状态(required)
     */
    private String HostStatus;
    /**
     * 本金银行主机流水号
     */
    private String CapHostSeqNo;
    /**
     * 利息银行主机流水号
     */
    private String IntHostSeqNo;

    public SinglePaymentOut() {
    }

    public SinglePaymentOut(String hostStatus) {
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

    public String getCapHostSeqNo() {
        return CapHostSeqNo;
    }

    public void setCapHostSeqNo(String capHostSeqNo) {
        CapHostSeqNo = capHostSeqNo;
    }

    public String getIntHostSeqNo() {
        return IntHostSeqNo;
    }

    public void setIntHostSeqNo(String intHostSeqNo) {
        IntHostSeqNo = intHostSeqNo;
    }

    @Override
    public String toString() {
        return "SinglePaymentOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", HostStatus='" + HostStatus + '\'' +
                ", CapHostSeqNo='" + CapHostSeqNo + '\'' +
                ", IntHostSeqNo='" + IntHostSeqNo + '\'' +
                '}';
    }
}
