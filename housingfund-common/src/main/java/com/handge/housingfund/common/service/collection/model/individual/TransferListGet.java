package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "TransferListGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferListGet implements Serializable {

    private String YWLSH;//业务流水号

    private String GRZH;//个人账号

    private String XingMing;//姓名

    private String ZYJE;//转移金额

    private String GRYHCKZHHM;//个人银行存款账户号码

    private String ZHYE;//个人账户余额

    private String JZNY;//缴至年月

    private String HBSJ;//合并时间

    private String ZRDWZH;//转入单位账号

    private String ZRDWM;//转入单位名

    private String ZCDWZH;//转出单位账号

    private String ZCDWM;//转出单位名称

    private String YWWD; //业务网点

    private String SHR;//审核人

    private String CZY; //操作人

    private String ZJHM;

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getGRZH() {
        return GRZH;
    }

    public void setGRZH(String GRZH) {
        this.GRZH = GRZH;
    }


    public String getZRDWZH() {
        return ZRDWZH;
    }

    public void setZRDWZH(String ZRDWZH) {
        this.ZRDWZH = ZRDWZH;
    }

    public String getZCDWZH() {
        return ZCDWZH;
    }

    public void setZCDWZH(String ZCDWZH) {
        this.ZCDWZH = ZCDWZH;
    }

    public String getZYJE() {
        return ZYJE;
    }

    public void setZYJE(String ZYJE) {
        this.ZYJE = ZYJE;
    }

    public String getGRYHCKZHHM() {
        return GRYHCKZHHM;
    }

    public void setGRYHCKZHHM(String GRYHCKZHHM) {
        this.GRYHCKZHHM = GRYHCKZHHM;
    }

    public String getZHYE() {
        return ZHYE;
    }

    public void setZHYE(String ZHYE) {
        this.ZHYE = ZHYE;
    }

    public String getJZNY() {
        return JZNY;
    }

    public void setJZNY(String JZNY) {
        this.JZNY = JZNY;
    }

    public String getHBSJ() {
        return HBSJ;
    }

    public void setHBSJ(String HBSJ) {
        this.HBSJ = HBSJ;
    }

    public String getZRDWM() {
        return ZRDWM;
    }

    public void setZRDWM(String ZRDWM) {
        this.ZRDWM = ZRDWM;
    }

    public String getZCDWM() {
        return ZCDWM;
    }

    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

    public void setZCDWM(String ZCDWM) {
        this.ZCDWM = ZCDWM;

    }

    public String getSHR() {
        return SHR;
    }

    public void setSHR(String SHR) {
        this.SHR = SHR;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    @Override
    public String toString() {
        return "TransferListGet{" +
            "YWLSH='" + YWLSH + '\'' +
            ", GRZH='" + GRZH + '\'' +
            ", XingMing='" + XingMing + '\'' +
            ", ZYJE='" + ZYJE + '\'' +
            ", GRYHCKZHHM='" + GRYHCKZHHM + '\'' +
            ", ZHYE='" + ZHYE + '\'' +
            ", JZNY='" + JZNY + '\'' +
            ", HBSJ='" + HBSJ + '\'' +
            ", ZRDWM='" + ZRDWM + '\'' +
            ", ZCDWM='" + ZCDWM + '\'' +
            ", YWWD='" + YWWD + '\'' +
            ", CZY='" + CZY + '\'' +
            ", SHR='" + SHR + '\'' +
            '}';
    }
}
