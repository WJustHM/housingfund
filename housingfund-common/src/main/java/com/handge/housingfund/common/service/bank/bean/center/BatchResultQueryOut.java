package com.handge.housingfund.common.service.bank.bean.center;

import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 批量业务结果查询(BDC107)输出格式
 */
public class BatchResultQueryOut implements Serializable {
    private static final long serialVersionUID = 7138573072289542072L;
    /**
     * CenterHeadOut(required)
     */
    private CenterHeadOut centerHeadOut;
    /**
     * 批量日期(required)
     */
    private String BatchDate;
    /**
     * 批量项目编号
     */
    private String BatchPrjNo;
    /**
     * 批量编号(required)
     */
    private String BatchNo;
    /**
     * 业务类型(required)
     */
    private String BusType;
    /**
     * 币种(required)
     */
    private String CurrNo;
    /**
     * 钞汇鉴别(required)
     */
    private int CurrIden;
    /**
     * 发送机构(required)
     */
    private String BatchSendBranchNo;
    /**
     * 发送操作员(required)
     */
    private String BkOperNo;
    /**
     * 发起方流水号
     */
    private String SendSeqNo;
    /**
     * 总笔数(required)
     */
    private int BatchTotalNum;
    /**
     * 总金额(required)
     */
    private BigDecimal BatchTotalAmt;
    /**
     * 成功笔数(required)
     */
    private int BatchTotalSuccNum;
    /**
     * 成功金额(required)
     */
    private BigDecimal BatchTotalSuccAmt;
    /**
     * 批量备注
     */
    private String Remark;
    /**
     * 发送机构
     */
    private String Summary;
    /**
     * 当前状态(required)
     */
    private String BatchTxStatus;

    public BatchResultQueryOut() {
    }

    public BatchResultQueryOut(CenterHeadOut centerHeadOut, String batchDate, String batchNo, String busType, String currNo, int currIden, String batchSendBranchNo, String bkOperNo, int batchTotalNum, BigDecimal batchTotalAmt, int batchTotalSuccNum, BigDecimal batchTotalSuccAmt, String batchTxStatus) {
        this.centerHeadOut = centerHeadOut;
        BatchDate = batchDate;
        BatchNo = batchNo;
        BusType = busType;
        CurrNo = currNo;
        CurrIden = currIden;
        BatchSendBranchNo = batchSendBranchNo;
        BkOperNo = bkOperNo;
        BatchTotalNum = batchTotalNum;
        BatchTotalAmt = batchTotalAmt;
        BatchTotalSuccNum = batchTotalSuccNum;
        BatchTotalSuccAmt = batchTotalSuccAmt;
        BatchTxStatus = batchTxStatus;
    }

    public CenterHeadOut getCenterHeadOut() {
        return centerHeadOut;
    }

    public void setCenterHeadOut(CenterHeadOut centerHeadOut) {
        this.centerHeadOut = centerHeadOut;
    }

    public String getBatchDate() {
        return BatchDate;
    }

    public void setBatchDate(String batchDate) {
        BatchDate = batchDate;
    }

    public String getBatchPrjNo() {
        return BatchPrjNo;
    }

    public void setBatchPrjNo(String batchPrjNo) {
        BatchPrjNo = batchPrjNo;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    public String getBusType() {
        return BusType;
    }

    public void setBusType(String busType) {
        BusType = busType;
    }

    public String getCurrNo() {
        return CurrNo;
    }

    public void setCurrNo(String currNo) {
        CurrNo = currNo;
    }

    public int getCurrIden() {
        return CurrIden;
    }

    public void setCurrIden(int currIden) {
        CurrIden = currIden;
    }

    public String getBatchSendBranchNo() {
        return BatchSendBranchNo;
    }

    public void setBatchSendBranchNo(String batchSendBranchNo) {
        BatchSendBranchNo = batchSendBranchNo;
    }

    public String getBkOperNo() {
        return BkOperNo;
    }

    public void setBkOperNo(String bkOperNo) {
        BkOperNo = bkOperNo;
    }

    public String getSendSeqNo() {
        return SendSeqNo;
    }

    public void setSendSeqNo(String sendSeqNo) {
        SendSeqNo = sendSeqNo;
    }

    public int getBatchTotalNum() {
        return BatchTotalNum;
    }

    public void setBatchTotalNum(int batchTotalNum) {
        BatchTotalNum = batchTotalNum;
    }

    public BigDecimal getBatchTotalAmt() {
        return BatchTotalAmt;
    }

    public void setBatchTotalAmt(BigDecimal batchTotalAmt) {
        BatchTotalAmt = batchTotalAmt;
    }

    public int getBatchTotalSuccNum() {
        return BatchTotalSuccNum;
    }

    public void setBatchTotalSuccNum(int batchTotalSuccNum) {
        BatchTotalSuccNum = batchTotalSuccNum;
    }

    public BigDecimal getBatchTotalSuccAmt() {
        return BatchTotalSuccAmt;
    }

    public void setBatchTotalSuccAmt(BigDecimal batchTotalSuccAmt) {
        BatchTotalSuccAmt = batchTotalSuccAmt;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getBatchTxStatus() {
        return BatchTxStatus;
    }

    public void setBatchTxStatus(String batchTxStatus) {
        BatchTxStatus = batchTxStatus;
    }

    @Override
    public String toString() {
        return "BatchResultQueryOut{" +
                "centerHeadOut=" + centerHeadOut +
                ", BatchDate='" + BatchDate + '\'' +
                ", BatchPrjNo=" + BatchPrjNo +
                ", BatchNo='" + BatchNo + '\'' +
                ", BusType='" + BusType + '\'' +
                ", CurrNo='" + CurrNo + '\'' +
                ", CurrIden=" + CurrIden +
                ", BatchSendBranchNo='" + BatchSendBranchNo + '\'' +
                ", BkOperNo='" + BkOperNo + '\'' +
                ", SendSeqNo='" + SendSeqNo + '\'' +
                ", BatchTotalNum=" + BatchTotalNum +
                ", BatchTotalAmt=" + BatchTotalAmt +
                ", BatchTotalSuccNum=" + BatchTotalSuccNum +
                ", BatchTotalSuccAmt=" + BatchTotalSuccAmt +
                ", Remark='" + Remark + '\'' +
                ", Summary='" + Summary + '\'' +
                ", BatchTxStatus='" + BatchTxStatus + '\'' +
                '}';
    }
}
