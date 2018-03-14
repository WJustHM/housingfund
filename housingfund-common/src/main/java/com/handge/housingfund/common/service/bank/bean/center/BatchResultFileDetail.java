package com.handge.housingfund.common.service.bank.bean.center;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 批量业务结果文件明细行
 * 明细行:序号|币种|钞汇|金额|付方账号|付方户名|付方余额|收方账号|收方户名|收方余额|响应码|响应信息|主机日期|主机流水号|回执编号|银行扩展|
 * Created by gxy on 17-8-4.
 */
public class BatchResultFileDetail implements Serializable {
    private static final long serialVersionUID = -6076097538445804086L;
    /**
     * 序号
     */
    private String no;
    /**
     * 币种
     */
    private String CurrNo;
    /**
     * 钞汇
     */
    private String CurrIden;
    /**
     * 金额
     */
    private BigDecimal amt;
    /**
     * 付方账号
     */
    private String DeAcctNo;
    /**
     * 付方户名
     */
    private String DeAcctName;
    /**
     * 付方余额
     */
    private BigDecimal deBalance;
    /**
     * 收方账号
     */
    private String CrAcctNo;
    /**
     * 收方户名
     */
    private String CrAcctName;
    /**
     * 收方余额
     */
    private BigDecimal crBanlance;
    /**
     * 响应码
     */
    private String rtnCode;
    /**
     * 响应信息
     */
    private String rtnMsg;
    /**
     * 主机日期
     */
    private String hostDate;
    /**
     * 主机流水号
     */
    private String hostSeqNo;
    /**
     * 回执编号
     */
    private String receiptNo;
    /**
     * 银行扩展
     */
    private String bankExtend;

    public BatchResultFileDetail() {
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getCurrNo() {
        return CurrNo;
    }

    public void setCurrNo(String currNo) {
        CurrNo = currNo;
    }

    public String getCurrIden() {
        return CurrIden;
    }

    public void setCurrIden(String currIden) {
        CurrIden = currIden;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public String getDeAcctNo() {
        return DeAcctNo;
    }

    public void setDeAcctNo(String deAcctNo) {
        DeAcctNo = deAcctNo;
    }

    public String getDeAcctName() {
        return DeAcctName;
    }

    public void setDeAcctName(String deAcctName) {
        DeAcctName = deAcctName;
    }

    public BigDecimal getDeBalance() {
        return deBalance;
    }

    public void setDeBalance(BigDecimal deBalance) {
        this.deBalance = deBalance;
    }

    public String getCrAcctNo() {
        return CrAcctNo;
    }

    public void setCrAcctNo(String crAcctNo) {
        CrAcctNo = crAcctNo;
    }

    public String getCrAcctName() {
        return CrAcctName;
    }

    public void setCrAcctName(String crAcctName) {
        CrAcctName = crAcctName;
    }

    public BigDecimal getCrBanlance() {
        return crBanlance;
    }

    public void setCrBanlance(BigDecimal crBanlance) {
        this.crBanlance = crBanlance;
    }

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getRtnMsg() {
        return rtnMsg;
    }

    public void setRtnMsg(String rtnMsg) {
        this.rtnMsg = rtnMsg;
    }

    public String getHostDate() {
        return hostDate;
    }

    public void setHostDate(String hostDate) {
        this.hostDate = hostDate;
    }

    public String getHostSeqNo() {
        return hostSeqNo;
    }

    public void setHostSeqNo(String hostSeqNo) {
        this.hostSeqNo = hostSeqNo;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getBankExtend() {
        return bankExtend;
    }

    public void setBankExtend(String bankExtend) {
        this.bankExtend = bankExtend;
    }

    @Override
    public String toString() {
        return "BatchResultFileDetail{" +
                "no='" + no + '\'' +
                ", CurrNo='" + CurrNo + '\'' +
                ", CurrIden='" + CurrIden + '\'' +
                ", amt=" + amt +
                ", DeAcctNo='" + DeAcctNo + '\'' +
                ", DeAcctName='" + DeAcctName + '\'' +
                ", deBalance=" + deBalance +
                ", CrAcctNo='" + CrAcctNo + '\'' +
                ", CrAcctName='" + CrAcctName + '\'' +
                ", crBanlance=" + crBanlance +
                ", rtnCode='" + rtnCode + '\'' +
                ", rtnMsg='" + rtnMsg + '\'' +
                ", hostDate='" + hostDate + '\'' +
                ", hostSeqNo='" + hostSeqNo + '\'' +
                ", receiptNo='" + receiptNo + '\'' +
                ", bankExtend='" + bankExtend + '\'' +
                '}';
    }
}
