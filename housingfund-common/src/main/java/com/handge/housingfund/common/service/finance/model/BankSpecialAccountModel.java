package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by xuefei_wang on 17-8-17.
 */
@XmlRootElement(name = "银行专户")
@XmlAccessorType(XmlAccessType.FIELD)
public class BankSpecialAccountModel implements Serializable {
    @XmlElement(name = "银行名称")
    private String bankName;

    @XmlElement(name = "银行代码")
    private String bankCode;

    @XmlElement(name = "银行专户名称")
    private String accountName;

    @XmlElement(name = "专户性质")
    private String accountAttribution;

    @XmlElement(name = "银行网点")
    private String workstation;

    @XmlElement(name = "科目编号")
    private String subjectId;

    @XmlElement(name = "银行专户号码")
    private String accountId;

    @XmlElement(name = "专户状态")
    private String status;

    @XmlElement(name = "状态日期")
    private String statusTime;

    @XmlElement(name = "开户日期")
    private String createTime;

    @XmlElement(name = "备注")
    private String note;

    public BankSpecialAccountModel() {
        this(null,null,null,null,null,null,null,null,null,null,null);
    }

    public BankSpecialAccountModel(String bankName, String bankCode, String accountName, String accountAttribution, String workstation, String subjectId, String accountId, String status, String statusTime, String createTime, String note) {
        this.bankName = bankName;
        this.bankCode = bankCode;
        this.accountName = accountName;
        this.accountAttribution = accountAttribution;
        this.workstation = workstation;
        this.subjectId = subjectId;
        this.accountId = accountId;
        this.status = status;
        this.statusTime = statusTime;
        this.createTime = createTime;
        this.note = note;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountAttribution() {
        return accountAttribution;
    }

    public void setAccountAttribution(String accountAttribution) {
        this.accountAttribution = accountAttribution;
    }

    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "BankSpecialAccountModel{" +
                "bankName='" + bankName + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", accountName='" + accountName + '\'' +
                ", accountAttribution='" + accountAttribution + '\'' +
                ", workstation='" + workstation + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", status='" + status + '\'' +
                ", statusTime='" + statusTime + '\'' +
                ", createTime='" + createTime + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
