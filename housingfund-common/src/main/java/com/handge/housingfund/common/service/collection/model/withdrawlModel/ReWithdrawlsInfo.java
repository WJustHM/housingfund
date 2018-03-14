package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/7/13.
 * 描述
 */
@XmlRootElement(name = "ReWithdrawlsInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReWithdrawlsInfo implements Serializable {
    /**
     * withdrawlsInfo 提取信息
     * BLZL 办理资料
     * CZY 操作员
     * YWWD 业务网点
     */
    private WithdrawlsInfo withdrawlsInfo;
    private WithdrawlReceiBankInfo withdrawlReceiBankInfo;
    @XmlElement(name = "BLZL")
    private String blzl;
    @XmlElement(name = "CZY")
    private String czy;
    @XmlElement(name = "YWWD")
    private String ywwd;
    @XmlElement(name = "YWLSH")
    private String ywlsh;

    public WithdrawlReceiBankInfo getWithdrawlReceiBankInfo() {
        return withdrawlReceiBankInfo;
    }

    public void setWithdrawlReceiBankInfo(WithdrawlReceiBankInfo withdrawlReceiBankInfo) {
        this.withdrawlReceiBankInfo = withdrawlReceiBankInfo;
    }

    public String getYwlsh() {
        return ywlsh;
    }

    public void setYwlsh(String ywlsh) {
        this.ywlsh = ywlsh;
    }

    public WithdrawlsInfo getWithdrawlsInfo() {
        return withdrawlsInfo;
    }

    public void setWithdrawlsInfo(WithdrawlsInfo withdrawlsInfo) {
        this.withdrawlsInfo = withdrawlsInfo;
    }

    public String getBlzl() {
        return blzl;
    }

    public void setBlzl(String blzl) {
        this.blzl = blzl;
    }

    public String getCzy() {
        return czy;
    }

    public void setCzy(String czy) {
        this.czy = czy;
    }

    public String getYwwd() {
        return ywwd;
    }

    public void setYwwd(String ywwd) {
        this.ywwd = ywwd;
    }

    @Override
    public String toString() {
        return "ReWithdrawlsInfo{" +
                ", withdrawlsInfo=" + withdrawlsInfo +
                ", blzl='" + blzl + '\'' +
                ", czy='" + czy + '\'' +
                ", ywwd='" + ywwd + '\'' +
                '}';
    }
}
