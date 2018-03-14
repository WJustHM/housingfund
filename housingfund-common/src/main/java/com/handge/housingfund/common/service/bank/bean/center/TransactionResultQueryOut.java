package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;

/**
 * 交易结果查询(BDC110)输出格式
 */
public class TransactionResultQueryOut implements Serializable{
    private static final long serialVersionUID = -4105226939649377349L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 原交易状态(required)
     */
    private String OldTxStatus;
    /**
     * 银行主机流水号
     */
    private String HostSeqNo;
    /**
     * 利息银行主机流水号
     */
    private String IntHostSeqN;
    /**
     * 贷款罚息银行主机流水号
     */
    private String PenHostSeqNo;
    /**
     * 贷款违约金银行主机流水号
     */
    private String FineHostSeqNo;
    /**
     * 原交易批量编号
     */
    private String OldBatchNo;

    public TransactionResultQueryOut() {
    }

    public TransactionResultQueryOut(CenterHeadOut centerHeadOut, String oldTxStatus) {
        this.centerHeadOut = centerHeadOut;
        OldTxStatus = oldTxStatus;
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    public String getOldTxStatus() {
        return OldTxStatus;
    }

    public void setOldTxStatus(String oldTxStatus) {
        OldTxStatus = oldTxStatus;
    }

    public String getHostSeqNo() {
        return HostSeqNo;
    }

    public void setHostSeqNo(String hostSeqNo) {
        HostSeqNo = hostSeqNo;
    }

    public String getIntHostSeqN() {
        return IntHostSeqN;
    }

    public void setIntHostSeqN(String intHostSeqN) {
        IntHostSeqN = intHostSeqN;
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

    public String getOldBatchNo() {
        return OldBatchNo;
    }

    public void setOldBatchNo(String oldBatchNo) {
        OldBatchNo = oldBatchNo;
    }

    @Override
    public String toString() {
        return "TransactionResultQueryOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", OldTxStatus='" + OldTxStatus + '\'' +
                ", HostSeqNo='" + HostSeqNo + '\'' +
                ", IntHostSeqN='" + IntHostSeqN + '\'' +
                ", PenHostSeqNo='" + PenHostSeqNo + '\'' +
                ", FineHostSeqNo='" + FineHostSeqNo + '\'' +
                ", OldBatchNo='" + OldBatchNo + '\'' +
                '}';
    }
}
