package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

public class WithdrawlReceiBankInfo implements Serializable
{
    /**
     * @param yhzhhm 银行账户号码
     * @param yhhm 银行户名
     * @param yhmc 银行名称
     *
     *
     */
    @XmlElement(name = "YHZHHM")
    private String yhzhhm;
    @XmlElement(name = "YHHM")
    private String yhhm;
    @XmlElement(name = "YHMC")
    private String yhmc;

    public String getYhzhhm() {
        return yhzhhm;
    }

    public void setYhzhhm(String yhzhhm) {
        this.yhzhhm = yhzhhm;
    }

    public String getYhhm() {
        return yhhm;
    }

    public void setYhhm(String yhhm) {
        this.yhhm = yhhm;
    }

    public String getYhmc() {
        return yhmc;
    }

    public void setYhmc(String yhmc) {
        this.yhmc = yhmc;
    }
}
