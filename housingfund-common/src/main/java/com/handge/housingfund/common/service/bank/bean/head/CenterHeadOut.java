package com.handge.housingfund.common.service.bank.bean.head;

import java.io.Serializable;

/**
 * 公积金中心发起交易输出格式报文头
 */
public class CenterHeadOut implements Serializable {
    private static final long serialVersionUID = 4538995276281255915L;
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

    public CenterHeadOut() {
    }

    /**
     * 实例化公积金中心输出格式报文头对象
     * @param txStatus
     * @param rtnCode
     * @param rtnMessage
     * @param receiveDate
     * @param receiveTime
     * @param receiveSeqNo
     * @param receiveNode
     * @param txCode
     * @param BDCDate
     * @param BDCTime
     * @param BDCSeqNo
     * @param sendNode
     */
    public CenterHeadOut(String txStatus, String rtnCode, String rtnMessage, String receiveDate, String receiveTime, String receiveSeqNo, String receiveNode, String txCode, String BDCDate, String BDCTime, String BDCSeqNo, String sendNode) {
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

    public void setTxStatus(String txStatus) {
        TxStatus = txStatus;
    }

    public String getRtnCode() {
        return RtnCode;
    }

    public void setRtnCode(String rtnCode) {
        RtnCode = rtnCode;
    }

    public String getRtnMessage() {
        return RtnMessage;
    }

    public void setRtnMessage(String rtnMessage) {
        RtnMessage = rtnMessage;
    }

    public String getReceiveDate() {
        return ReceiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        ReceiveDate = receiveDate;
    }

    public String getReceiveTime() {
        return ReceiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        ReceiveTime = receiveTime;
    }

    public String getReceiveSeqNo() {
        return ReceiveSeqNo;
    }

    public void setReceiveSeqNo(String receiveSeqNo) {
        ReceiveSeqNo = receiveSeqNo;
    }

    public String getReceiveNode() {
        return ReceiveNode;
    }

    public void setReceiveNode(String receiveNode) {
        ReceiveNode = receiveNode;
    }

    public String getTxCode() {
        return TxCode;
    }

    public void setTxCode(String txCode) {
        TxCode = txCode;
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

    public String getSendNode() {
        return SendNode;
    }

    public void setSendNode(String sendNode) {
        SendNode = sendNode;
    }

    @Override
    public String toString() {
        return "CenterHeadOut{" +
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
