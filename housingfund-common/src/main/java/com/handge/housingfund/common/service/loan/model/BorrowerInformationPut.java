package com.handge.housingfund.common.service.loan.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/8/16.
 */
@XmlRootElement(name = "BorrowerInformationPut")
@XmlAccessorType(XmlAccessType.FIELD)
public class BorrowerInformationPut implements Serializable {
    private String JCD;//缴存地
    
    private String NianLing;//年龄
    
    private String JKZK;//健康状况
    
    private String YGXZ;//用工性质
    
    private String ZYJJLY;//主要经济来源
    
    private String YSR;//月收入
    
    private String JTYSR;//家庭月收入
    
    private String JKRBLZL;//借款人办理资料

    private String JKRGJGZH;//借款人个人公积金账号

    private String SFWTKHJCE;//是否委托扣划缴存额

    private String HKZH;//还款账号

    private String ZHKHYHMC;//账户开户银行开户名称

    private String CZY;//操作员

    private String YWWD;//业务网点

    public String getJCD() {
        return JCD;
    }

    public void setJCD(String JCD) {
        this.JCD = JCD;
    }

    public String getNianLing() {
        return NianLing;
    }

    public void setNianLing(String nianLing) {
        NianLing = nianLing;
    }

    public String getJKZK() {
        return JKZK;
    }

    public void setJKZK(String JKZK) {
        this.JKZK = JKZK;
    }

    public String getYGXZ() {
        return YGXZ;
    }

    public void setYGXZ(String YGXZ) {
        this.YGXZ = YGXZ;
    }

    public String getZYJJLY() {
        return ZYJJLY;
    }

    public void setZYJJLY(String ZYJJLY) {
        this.ZYJJLY = ZYJJLY;
    }

    public String getYSR() {
        return YSR;
    }

    public void setYSR(String YSR) {
        this.YSR = YSR;
    }

    public String getJTYSR() {
        return JTYSR;
    }

    public void setJTYSR(String JTYSR) {
        this.JTYSR = JTYSR;
    }

    public String getJKRBLZL() {
        return JKRBLZL;
    }

    public void setJKRBLZL(String JKRBLZL) {
        this.JKRBLZL = JKRBLZL;
    }

    public String getJKRGJGZH() {
        return JKRGJGZH;
    }

    public void setJKRGJGZH(String JKRGJGZH) {
        this.JKRGJGZH = JKRGJGZH;
    }

    public String getSFWTKHJCE() {
        return SFWTKHJCE;
    }

    public void setSFWTKHJCE(String SFWTKHJCE) {
        this.SFWTKHJCE = SFWTKHJCE;
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
        return "BorrowerInformationPut{" +
                "JCD='" + JCD + '\'' +
                ", NianLing='" + NianLing + '\'' +
                ", JKZK='" + JKZK + '\'' +
                ", YGXZ='" + YGXZ + '\'' +
                ", ZYJJLY='" + ZYJJLY + '\'' +
                ", YSR='" + YSR + '\'' +
                ", JTYSR='" + JTYSR + '\'' +
                ", JKRBLZL='" + JKRBLZL + '\'' +
                ", JKRGJGZH='" + JKRGJGZH + '\'' +
                ", SFWTKHJCE='" + SFWTKHJCE + '\'' +
                ", HKZH='" + HKZH + '\'' +
                ", ZHKHYHMC='" + ZHKHYHMC + '\'' +
                ", CZY='" + CZY + '\'' +
                ", YWWD='" + YWWD + '\'' +
                '}';
    }
}
