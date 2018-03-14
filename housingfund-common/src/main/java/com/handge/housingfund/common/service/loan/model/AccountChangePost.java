package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "AccountChangePost")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountChangePost  implements Serializable {

    private static final long serialVersionUID = -9164191792446934962L;
    private String DKZH;  //贷款账户

    private String JKRXM;  //借款人姓名

    private String JKHTBH;  //借款合同编号

    private String HKZH;  //还款账号

    private String ZHKHYHMC;  //账号开户银行名称

    private String XHKZH;  //新还款账号

    private String XZHKHYHMC;  //新账号开户银行名称


    public String getDKZH() {
        return DKZH;
    }

    public void setDKZH(String DKZH) {
        this.DKZH = DKZH;
    }

    public String getJKRXM() {
        return JKRXM;
    }

    public void setJKRXM(String JKRXM) {
        this.JKRXM = JKRXM;
    }

    public String getJKHTBH() {
        return JKHTBH;
    }

    public void setJKHTBH(String JKHTBH) {
        this.JKHTBH = JKHTBH;
    }

    public String getHKZH() {
        return HKZH;
    }

    public void setHKZH(String HKZH) {
        this.HKZH = HKZH;
    }

    public String getZHKHYHMC() {
        return ZHKHYHMC;
    }

    public void setZHKHYHMC(String ZHKHYHMC) {
        this.ZHKHYHMC = ZHKHYHMC;
    }

    public String getXHKZH() {
        return XHKZH;
    }

    public void setXHKZH(String XHKZH) {
        this.XHKZH = XHKZH;
    }

    public String getXZHKHYHMC() {
        return XZHKHYHMC;
    }

    public void setXZHKHYHMC(String XZHKHYHMC) {
        this.XZHKHYHMC = XZHKHYHMC;
    }

    @Override
    public String toString() {
        return "AccountChangePost{" +
                "DKZH='" + DKZH + '\'' +
                ", JKRXM='" + JKRXM + '\'' +
                ", JKHTBH='" + JKHTBH + '\'' +
                ", HKZH='" + HKZH + '\'' +
                ", ZHKHYHMC='" + ZHKHYHMC + '\'' +
                ", XHKZH='" + XHKZH + '\'' +
                ", XZHKHYHMC='" + XZHKHYHMC + '\'' +
                '}';
    }
}