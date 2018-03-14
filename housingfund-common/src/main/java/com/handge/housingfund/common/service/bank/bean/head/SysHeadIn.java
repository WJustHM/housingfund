package com.handge.housingfund.common.service.bank.bean.head;

import java.io.Serializable;

/**
 * 数据应用系统发起交易输入格式报文头
 */
public class SysHeadIn implements Serializable {
    private static final long serialVersionUID = 7881115473080934312L;
    /**
     * 交易状态(required)
     */
    private String TxStatus;
    /**
     * 返回码(required)
     */
    private String RtnCode;
    /**
     * 返回信息(required)
     */
    private String RtnMessage;
    /**
     * 接收方日期(required)
     */
    private String ReceiveDate;
    /**
     * 接收方时间(required)
     */
    private String ReceiveTime;
    /**
     * 接收方流水号(required)
     */
    private String ReceiveSeqNo;
    /**
     * 接收方节点号(required)
     */
    private String ReceiveNode;
    /**
     * 交易代码(required)
     */
    private String TxCode;
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
    /**
     * 发送方节点号(required)
     */
    private String SendNode;

    public SysHeadIn() {
    }

    public SysHeadIn(String txStatus, String rtnCode, String rtnMessage, String receiveDate, String receiveTime, String receiveSeqNo, String receiveNode, String txCode, String BDCDate, String BDCTime, String BDCSeqNo, String sendNode) {
        TxStatus = txStatus;
        RtnCode = rtnCode;
        RtnMessage = rtnMessage;
        ReceiveDate = receiveDate;
        ReceiveTime = receiveTime;
        ReceiveSeqNo = receiveSeqNo;
        ReceiveNode = receiveNode;
        TxCode = txCode;
        this.BDCDate = BDCDate;
        this.BDCTime = BDCTime;
        this.BDCSeqNo = BDCSeqNo;
        SendNode = sendNode;
    }

    public String getTxStatus() {
        return TxStatus;
    }

    public String getRtnCode() {
        return RtnCode;
    }

    public String getRtnMessage() {
        return RtnMessage;
    }

    public String getReceiveDate() {
        return ReceiveDate;
    }

    public String getReceiveTime() {
        return ReceiveTime;
    }

    public String getReceiveSeqNo() {
        return ReceiveSeqNo;
    }

    public String getReceiveNode() {
        return ReceiveNode;
    }

    public String getTxCode() {
        return TxCode;
    }

    public String getBDCDate() {
        return BDCDate;
    }

    public String getBDCTime() {
        return BDCTime;
    }

    public String getBDCSeqNo() {
        return BDCSeqNo;
    }

    public String getSendNode() {
        return SendNode;
    }

    public void setTxStatus(String txStatus) {
        TxStatus = txStatus;
    }

    public void setRtnCode(String rtnCode) {
        RtnCode = rtnCode;
    }

    public void setRtnMessage(String rtnMessage) {
        RtnMessage = rtnMessage;
    }

    public void setReceiveDate(String receiveDate) {
        ReceiveDate = receiveDate;
    }

    public void setReceiveTime(String receiveTime) {
        ReceiveTime = receiveTime;
    }

    public void setReceiveSeqNo(String receiveSeqNo) {
        ReceiveSeqNo = receiveSeqNo;
    }

    public void setReceiveNode(String receiveNode) {
        ReceiveNode = receiveNode;
    }

    public void setTxCode(String txCode) {
        TxCode = txCode;
    }

    public void setBDCDate(String BDCDate) {
        this.BDCDate = BDCDate;
    }

    public void setBDCTime(String BDCTime) {
        this.BDCTime = BDCTime;
    }

    public void setBDCSeqNo(String BDCSeqNo) {
        this.BDCSeqNo = BDCSeqNo;
    }

    public void setSendNode(String sendNode) {
        SendNode = sendNode;
    }

    @Override
    public String toString() {
        return "SysHeadIn{" +
                "TxStatus='" + TxStatus + '\'' +
                ", RtnCode='" + RtnCode + '\'' +
                ", RtnMessage='" + RtnMessage + '\'' +
                ", ReceiveDate='" + ReceiveDate + '\'' +
                ", ReceiveTime='" + ReceiveTime + '\'' +
                ", ReceiveSeqNo='" + ReceiveSeqNo + '\'' +
                ", ReceiveNode='" + ReceiveNode + '\'' +
                ", TxCode='" + TxCode + '\'' +
                ", BDCDate='" + BDCDate + '\'' +
                ", BDCTime='" + BDCTime + '\'' +
                ", BDCSeqNo='" + BDCSeqNo + '\'' +
                ", SendNode='" + SendNode + '\'' +
                '}';
    }
}
