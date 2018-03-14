package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 贷款本息分解(BDC106)输出格式
 */
public class LoanCapIntDecOut implements Serializable {
    private static final long serialVersionUID = -1145732625896598886L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 本金银行主机流水号(required)
     */
    private String CapHostSeqNo;
    /**
     * 利息银行主机流水号
     */
    private String IntHostSeqNo;
    /**
     * 罚息银行主机流水号
     */
    private String PenHostSeqNo;
    /**
     * 违约金银行主机流水号
     */
    private String FineHostSeqNo;

    public LoanCapIntDecOut() {
    }

    public LoanCapIntDecOut(CenterHeadOut centerHeadOut, String capHostSeqNo, String intHostSeqNo, String penHostSeqNo, String fineHostSeqNo) {
        this.centerHeadOut = centerHeadOut;
        CapHostSeqNo = capHostSeqNo;
        IntHostSeqNo = intHostSeqNo;
        PenHostSeqNo = penHostSeqNo;
        FineHostSeqNo = fineHostSeqNo;
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
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

    public String getPenHostSeqNo() {
        return PenHostSeqNo;
    }

    public void setPenHostSeqNo(String penHostSeqNo) {
        PenHostSeqNo = penHostSeqNo;
    }

    public String getFineHostSeqNo() {
        return FineHostSeqNo;
    }

    public void setFineHostSeqNo(String fineHostSeqNo) {
        FineHostSeqNo = fineHostSeqNo;
    }

    @Override
    public String toString() {
        return "LoanCapIntDecOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", CapHostSeqNo='" + CapHostSeqNo + '\'' +
                ", IntHostSeqNo='" + IntHostSeqNo + '\'' +
                ", PenHostSeqNo='" + PenHostSeqNo + '\'' +
                ", FineHostSeqNo='" + FineHostSeqNo + '\'' +
                '}';
    }
}
