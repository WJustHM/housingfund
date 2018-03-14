package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import com.handge.housingfund.common.service.account.model.PageRes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/7/28.
 * 描述
 */
@XmlRootElement(name = "WithdrawlRecordsReturn")
@XmlAccessorType(XmlAccessType.FIELD)
public class WithdrawlRecordsReturn implements Serializable {
    String TZRQ;//填制日期
    IndiAcctInfo indiAcctInfo;//个人账户信息
    PageRes<Record> recordsList;//提取记录
    String CZY;//操作员
    String YWWD;//业务网点

    public String getTZRQ() {
        return TZRQ;
    }

    public void setTZRQ(String TZRQ) {
        this.TZRQ = TZRQ;
    }

    public IndiAcctInfo getIndiAcctInfo() {
        return indiAcctInfo;
    }

    public void setIndiAcctInfo(IndiAcctInfo indiAcctInfo) {
        this.indiAcctInfo = indiAcctInfo;
    }

    public PageRes<Record> getRecordsList() {
        return recordsList;
    }

    public void setRecordsList(PageRes<Record> recordsList) {
        this.recordsList = recordsList;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    @Override
    public String toString() {
        return "WithdrawlRecordsReturn{" +
                "TZRQ='" + TZRQ + '\'' +
                ", indiAcctInfo=" + indiAcctInfo +
                ", recordsList=" + recordsList +
                ", CZY='" + CZY + '\'' +
                ", YWWD='" + YWWD + '\'' +
                '}';
    }
}
