package com.handge.housingfund.common.service.bank.bean.head;

import java.io.Serializable;

/**
 * 公积金中心发起交易输入格式报文头
 */
public class CenterHeadIn implements Serializable {
    private static final long serialVersionUID = -1755802725193407999L;
    /**
     * 发送日期
     */
    private String SendDate;
    /**
     * 发送方时间
     */
    private String SendTime;
    /**
     * 业务流水号(required)
     */
    private String SendSeqNo;
    /**
     * 交易机构号
     */
    private String TxUnitNo;
    /**
     * 发送方节点号
     */
    private String SendNode;
    /**
     * 交易代码
     */
    private String TxCode;
    /**
     * 接收方节点号(required)
     */
    private String ReceiveNode;
    /**
     * 客户编号
     */
    private String CustNo;
    /**
     * 操作员编号(required)
     */
    private String OperNo;

    public CenterHeadIn() {
    }

    /**
     * 实例化公积金中心输入格式报文头对象,此构造函数只包含必填属性字段,可选字段使用set方法设置
     * @param sendDate
     * @param sendTime
     * @param sendSeqNo
     * @param txUnitNo
     * @param sendNode
     * @param txCode
     * @param receiveNode
     * @param operNo
     */
    public CenterHeadIn(String sendDate, String sendTime, String sendSeqNo, String txUnitNo, String sendNode, String txCode, String receiveNode, String operNo) {
        SendDate = sendDate;
        SendTime = sendTime;
        SendSeqNo = sendSeqNo;
        TxUnitNo = txUnitNo;
        SendNode = sendNode;
        TxCode = txCode;
        ReceiveNode = receiveNode;
        OperNo = operNo;
    }

    public CenterHeadIn(String sendSeqNo, String receiveNode, String operNo) {
        SendSeqNo = sendSeqNo;
        ReceiveNode = receiveNode;
        OperNo = operNo;
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

    public String getTxUnitNo() {
        return TxUnitNo;
    }

    public void setTxUnitNo(String txUnitNo) {
        TxUnitNo = txUnitNo;
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

    public String getCustNo() {
        return CustNo;
    }

    public void setCustNo(String custNo) {
        CustNo = custNo;
    }

    public String getOperNo() {
        return OperNo;
    }

    public void setOperNo(String operNo) {
        OperNo = operNo;
    }

    @Override
    public String toString() {
        return "CenterHeadIn{" +
                "SendDate='" + SendDate + '\'' +
                ", SendTime='" + SendTime + '\'' +
                ", SendSeqNo='" + SendSeqNo + '\'' +
                ", TxUnitNo='" + TxUnitNo + '\'' +
                ", SendNode='" + SendNode + '\'' +
                ", TxCode='" + TxCode + '\'' +
                ", ReceiveNode='" + ReceiveNode + '\'' +
                ", CustNo='" + CustNo + '\'' +
                ", OperNo='" + OperNo + '\'' +
                '}';
    }
}
