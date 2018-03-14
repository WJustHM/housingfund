package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/7/14.
 * 描述
 */
@XmlRootElement(name = "ReceiptReturn")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReceiptReturn implements Serializable {
    /**
     * YZM 验证码
     * YWLSH 业务流水号
     * YWWD 业务网点
     * TZRQ 填制日期
     * BLRQZ 提取办理人签字
     * CZY 操作员
     * SHR 审核人
     */
    private String YZM;
    private String YWLSH;
    private String YWWD;
    private String TZRQ;
    private String TQBLRQZ;
    private String CZY;
    private String SHR;
    private ReceiptIndiAcctInfo receiptIndiAcctInfo;
    private ReceiptWithdrawlsInfo receiptWithdrawlsInfo;

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getYZM() {
        return YZM;
    }

    public void setYZM(String YZM) {
        this.YZM = YZM;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getTZRQ() {
        return TZRQ;
    }

    public void setTZRQ(String TZRQ) {
        this.TZRQ = TZRQ;
    }

    public String getTQBLRQZ() {
        return TQBLRQZ;
    }

    public void setTQBLRQZ(String TQBLRQZ) {
        this.TQBLRQZ = TQBLRQZ;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getSHR() {
        return SHR;
    }

    public void setSHR(String SHR) {
        this.SHR = SHR;
    }

    public ReceiptIndiAcctInfo getReceiptIndiAcctInfo() {
        return receiptIndiAcctInfo;
    }

    public void setReceiptIndiAcctInfo(ReceiptIndiAcctInfo receiptIndiAcctInfo) {
        this.receiptIndiAcctInfo = receiptIndiAcctInfo;
    }

    public ReceiptWithdrawlsInfo getReceiptWithdrawlsInfo() {
        return receiptWithdrawlsInfo;
    }

    public void setReceiptWithdrawlsInfo(ReceiptWithdrawlsInfo receiptWithdrawlsInfo) {
        this.receiptWithdrawlsInfo = receiptWithdrawlsInfo;
    }

    @Override
    public String toString() {
        return "ReceiptReturn{" +
                "YZM='" + YZM + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", TZRQ='" + TZRQ + '\'' +
                ", TQBLRQZ='" + TQBLRQZ + '\'' +
                ", CZY='" + CZY + '\'' +
                ", SHR='" + SHR + '\'' +
                ", receiptIndiAcctInfo=" + receiptIndiAcctInfo +
                ", receiptWithdrawlsInfo=" + receiptWithdrawlsInfo +
                '}';
    }
}
