package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 定期支取(BDC115)输出格式
 */
public class FixedDrawOut implements Serializable {
    private static final long serialVersionUID = 5922219588910623189L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 银行主机流水号(required)
     */
    private String HostSeqNo;

    public FixedDrawOut() {
    }

    public FixedDrawOut(CenterHeadOut centerHeadOut, String hostSeqNo) {
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
        return "FixedDrawOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", HostSeqNo='" + HostSeqNo + '\'' +
                '}';
    }
}
