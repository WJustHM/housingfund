package com.handge.housingfund.common;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/10/1.
 * 描述
 */
public class LoanWithdrawl implements Serializable {
    private String jkrgrzh;//个人账号
    private String gtjkrgrzh;
    private BigDecimal jkrfse;
    private BigDecimal gtjkrfse;
    private String yhzhhm;//银行专户号码
    private String yhdm;//银行代码
    private String ywwd;//贷款账号的网点
    private BigDecimal benjin;//本金
    private BigDecimal lixi;//利息

    public String getYwwd() {
        return ywwd;
    }

    public void setYwwd(String ywwd) {
        this.ywwd = ywwd;
    }

    public String getYhzhhm() {
        return yhzhhm;
    }

    public void setYhzhhm(String yhzhhm) {
        this.yhzhhm = yhzhhm;
    }

    public String getJkrgrzh() {
        return jkrgrzh;
    }

    public void setJkrgrzh(String jkrgrzh) {
        this.jkrgrzh = jkrgrzh;
    }

    public String getGtjkrgrzh() {
        return gtjkrgrzh;
    }

    public void setGtjkrgrzh(String gtjkrgrzh) {
        this.gtjkrgrzh = gtjkrgrzh;
    }

    public BigDecimal getJkrfse() {
        return jkrfse;
    }

    public void setJkrfse(BigDecimal jkrfse) {
        this.jkrfse = jkrfse;
    }

    public BigDecimal getGtjkrfse() {
        return gtjkrfse;
    }

    public void setGtjkrfse(BigDecimal gtjkrfse) {
        this.gtjkrfse = gtjkrfse;
    }

    public BigDecimal getBenjin() {
        return benjin;
    }

    public void setBenjin(BigDecimal benjin) {
        this.benjin = benjin;
    }

    public BigDecimal getLixi() {
        return lixi;
    }

    public void setLixi(BigDecimal lixi) {
        this.lixi = lixi;
    }

    public String getYhdm() {
        return yhdm;
    }

    public void setYhdm(String yhdm) {
        this.yhdm = yhdm;
    }


}
