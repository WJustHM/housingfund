package com.handge.housingfund.common.service.bank.bean.head;

import java.io.Serializable;

/**
 * 数据应用系统发起交易输出格式报文头
 */
public class SysHeadOut implements Serializable {
    private static final long serialVersionUID = 7521596504311425902L;
    /**
     * 发送方日期(required)
     */
    private String SendDate;
    /**
     * 发送方时间(required)
     */
    private String SendTime;
    /**
     * 发送方流水号(required)
     */
    private String SendSeqNo;
    /**
     * 发送方节点号(required)
     */
    private String SendNode;
    /**
     * 交易代码(required)
     */
    private String TxCode;
    /**
     * 接收方节点号(required)
     */
    private String ReceiveNode;
    /**
     * 数据应用系统日期(required)
     */
    private String BDCDate;
    /**
     * 数据应用系统时间(required)
     */
    private String BDCTime;
    /**
     * 数据应用系统流水号(required)
     */
    private String BDCSeqNo;

    public SysHeadOut() {
    }

    public SysHeadOut(String sendDate, String sendTime, String sendSeqNo, String sendNode, String txCode, String receiveNode, String BDCDate, String BDCTime, String BDCSeqNo) {
        SendDate = sendDate;
        SendTime = sendTime;
        SendSeqNo = sendSeqNo;
        SendNode = sendNode;
        TxCode = txCode;
        ReceiveNode = receiveNode;
        this.BDCDate = BDCDate;
        this.BDCTime = BDCTime;
        this.BDCSeqNo = BDCSeqNo;
    }

    public String getSendDate() {
        return SendDate;
    }

    public void setSendDate(String sendDate) {
        SendDate = sendDate;
    }

    public String getSendTime() {
        return SendTime;
    }

    public void setSendTime(String sendTime) {
        SendTime = sendTime;
    }

    public String getSendSeqNo() {
        return SendSeqNo;
    }

    public void setSendSeqNo(String sendSeqNo) {
        SendSeqNo = sendSeqNo;
    }

    public String getSendNode() {
        return SendNode;
    }

    public void setSendNode(String sendNode) {
        SendNode = sendNode;
    }

    public String getTxCode() {
        return TxCode;
    }

    public void setTxCode(String txCode) {
        TxCode = txCode;
    }

    public String getReceiveNode() {
        return ReceiveNode;
    }

    public void setReceiveNode(String receiveNode) {
        ReceiveNode = receiveNode;
    }

    public String getBDCDate() {
        return BDCDate;
    }

    public void setBDCDate(String BDCDate) {
        this.BDCDate = BDCDate;
    }

    public String getBDCTime() {
        return BDCTime;
    }

    public void setBDCTime(String BDCTime) {
        this.BDCTime = BDCTime;
    }

    public String getBDCSeqNo() {
        return BDCSeqNo;
    }

    public void setBDCSeqNo(String BDCSeqNo) {
        this.BDCSeqNo = BDCSeqNo;
    }

    @Override
    public String toString() {
        return "SysHeadOut{" +
                "SendDate='" + SendDate + '\'' +
                ", SendTime='" + SendTime + '\'' +
                ", SendSeqNo='" + SendSeqNo + '\'' +
                ", SendNode='" + SendNode + '\'' +
                ", TxCode='" + TxCode + '\'' +
                ", ReceiveNode='" + ReceiveNode + '\'' +
                ", BDCDate='" + BDCDate + '\'' +
                ", BDCTime='" + BDCTime + '\'' +
                ", BDCSeqNo='" + BDCSeqNo + '\'' +
                '}';
    }
}
