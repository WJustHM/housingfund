package com.handge.housingfund.common.service.collection.model.withdrawlModel;


import com.handge.housingfund.common.service.review.model.ReviewInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/7/15.
 * 描述 提取详细信息，包括个人账户信息、提取信息、办理资料
 */
@XmlRootElement(name = "WithdrawlsDetailInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class WithdrawlsDetailInfo implements Serializable {
    /**
     * indiAcctInfo 个人账户信息
     * withdrawlsInfo 提取信息
     * reviewInfos 审核历史记录
     * BLZL 办理资料
     * withdrawlReceiBankInfo 本次收款银行信息
     *
     */
    private IndiAcctInfo indiAcctInfo;
    private WithdrawlsInfo withdrawlsInfo;
    private ArrayList<ReviewInfo> reviewInfos;
    private WithdrawlReceiBankInfo withdrawlReceiBankInfo;
    @XmlElement(name = "BLZL")
    private String blzl;
    @XmlElement(name = "CZY")
    private String czy;
    @XmlElement(name = "YWWD")
    private String ywwd;
    @XmlElement(name = "YWLSH")
    private String ywlsh;
    @XmlElement(name = "TQHJ")//提取合计
    private String tqhj;


    public String getTqhj() {
        return tqhj;
    }

    public void setTqhj(String tqhj) {
        this.tqhj = tqhj;
    }

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

    public ArrayList<ReviewInfo> getReviewInfos() {
        return reviewInfos;
    }

    public void setReviewInfos(ArrayList<ReviewInfo> reviewInfos) {
        this.reviewInfos = reviewInfos;
    }

    public String getBlzl() {
        return blzl;
    }

    public void setBlzl(String blzl) {
        this.blzl = blzl;
    }

    public IndiAcctInfo getIndiAcctInfo() {
        return indiAcctInfo;
    }

    public void setIndiAcctInfo(IndiAcctInfo indiAcctInfo) {
        this.indiAcctInfo = indiAcctInfo;
    }

    public WithdrawlsInfo getWithdrawlsInfo() {
        return withdrawlsInfo;
    }

    public void setWithdrawlsInfo(WithdrawlsInfo withdrawlsInfo) {
        this.withdrawlsInfo = withdrawlsInfo;
    }

    @Override
    public String toString() {
        return "WithdrawlsDetailInfo{" +
                "indiAcctInfo=" + indiAcctInfo +
                ", withdrawlsInfo=" + withdrawlsInfo +
                ", reviewInfos=" + reviewInfos +
                ", blzl='" + blzl + '\'' +
                ", czy='" + czy + '\'' +
                ", ywwd='" + ywwd + '\'' +
                '}';
    }
}
