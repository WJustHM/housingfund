package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Liujuhao on 2018/3/1.
 */
@XmlRootElement(name = "EntrustDeductInfos")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntrustDeductInfos implements Serializable{

    private static final long serialVersionUID = -4439402126605773119L;

    String WTRXM;   //委托人项目
    String GRZFGJJZH;   //个人住房公积金账号
    String SFZH;    //身份证号
    String JKHTBH;  //借款合同编号
    String DKZH;    //贷款账号
    String GTJKRXM_1;   //共同借款人信息1
    String GTJKRGJJZH_1;
    String GTJKRSFZH_1;
    String GTJKRXM_2;   //共同借款人信息2
    String GTJKRGJJZH_2;
    String GTJKRSFZH_2;
    String SWTYWWD; //受委托业务网点

    @Override
    public String toString() {
        return "EntrustDeductInfos{" +
                "WTRXM='" + WTRXM + '\'' +
                ", GRZFGJJZH='" + GRZFGJJZH + '\'' +
                ", SFZH='" + SFZH + '\'' +
                ", JKHTBH='" + JKHTBH + '\'' +
                ", DKZH='" + DKZH + '\'' +
                ", GTJKRXM_1='" + GTJKRXM_1 + '\'' +
                ", GTJKRGJJZH_1='" + GTJKRGJJZH_1 + '\'' +
                ", GTJKRSFZH_1='" + GTJKRSFZH_1 + '\'' +
                ", GTJKRXM_2='" + GTJKRXM_2 + '\'' +
                ", GTJKRGJJZH_2='" + GTJKRGJJZH_2 + '\'' +
                ", GTJKRSFZH_2='" + GTJKRSFZH_2 + '\'' +
                ", SWTYWWD='" + SWTYWWD + '\'' +
                '}';
    }

    public String getWTRXM() {
        return WTRXM;
    }

    public void setWTRXM(String WTRXM) {
        this.WTRXM = WTRXM;
    }

    public String getGRZFGJJZH() {
        return GRZFGJJZH;
    }

    public void setGRZFGJJZH(String GRZFGJJZH) {
        this.GRZFGJJZH = GRZFGJJZH;
    }

    public String getSFZH() {
        return SFZH;
    }

    public void setSFZH(String SFZH) {
        this.SFZH = SFZH;
    }

    public String getJKHTBH() {
        return JKHTBH;
    }

    public void setJKHTBH(String JKHTBH) {
        this.JKHTBH = JKHTBH;
    }

    public String getDKZH() {
        return DKZH;
    }

    public void setDKZH(String DKZH) {
        this.DKZH = DKZH;
    }

    public String getGTJKRXM_1() {
        return GTJKRXM_1;
    }

    public void setGTJKRXM_1(String GTJKRXM_1) {
        this.GTJKRXM_1 = GTJKRXM_1;
    }

    public String getGTJKRGJJZH_1() {
        return GTJKRGJJZH_1;
    }

    public void setGTJKRGJJZH_1(String GTJKRGJJZH_1) {
        this.GTJKRGJJZH_1 = GTJKRGJJZH_1;
    }

    public String getGTJKRSFZH_1() {
        return GTJKRSFZH_1;
    }

    public void setGTJKRSFZH_1(String GTJKRSFZH_1) {
        this.GTJKRSFZH_1 = GTJKRSFZH_1;
    }

    public String getGTJKRXM_2() {
        return GTJKRXM_2;
    }

    public void setGTJKRXM_2(String GTJKRXM_2) {
        this.GTJKRXM_2 = GTJKRXM_2;
    }

    public String getGTJKRGJJZH_2() {
        return GTJKRGJJZH_2;
    }

    public void setGTJKRGJJZH_2(String GTJKRGJJZH_2) {
        this.GTJKRGJJZH_2 = GTJKRGJJZH_2;
    }

    public String getGTJKRSFZH_2() {
        return GTJKRSFZH_2;
    }

    public void setGTJKRSFZH_2(String GTJKRSFZH_2) {
        this.GTJKRSFZH_2 = GTJKRSFZH_2;
    }

    public String getSWTYWWD() {
        return SWTYWWD;
    }

    public void setSWTYWWD(String SWTYWWD) {
        this.SWTYWWD = SWTYWWD;
    }
}
