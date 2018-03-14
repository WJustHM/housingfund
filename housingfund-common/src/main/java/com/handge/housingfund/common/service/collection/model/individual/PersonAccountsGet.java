package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "PersonAccountsGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonAccountsGet    implements Serializable {

    private String GRZH;

    private String XimgMing;

    private String GRYHCKZHHM;

    private String GRYHCKKHYHMC;

    private String ZHZT;

    private String ZHYE;

    private String JZNY;

    private String DWMC;

    private String DWZH;

    private String SJHM;
    private String GRJCJS;

    public String getGRJCJS() {
        return GRJCJS;
    }

    public void setGRJCJS(String GRJCJS) {
        this.GRJCJS = GRJCJS;
    }
    public String getSJHM() {
        return SJHM;
    }

    public void setSJHM(String SJHM) {
        this.SJHM = SJHM;
    }

    public String getGRZH() {
        return GRZH;
    }

    public void setGRZH(String GRZH) {
        this.GRZH = GRZH;
    }

    public String getXimgMing() {
        return XimgMing;
    }

    public void setXimgMing(String ximgMing) {
        XimgMing = ximgMing;
    }

    public String getGRYHCKZHHM() {
        return GRYHCKZHHM;
    }

    public void setGRYHCKZHHM(String GRYHCKZHHM) {
        this.GRYHCKZHHM = GRYHCKZHHM;
    }

    public String getGRYHCKKHYHMC() {
        return GRYHCKKHYHMC;
    }

    public void setGRYHCKKHYHMC(String GRYHCKKHYHMC) {
        this.GRYHCKKHYHMC = GRYHCKKHYHMC;
    }

    public String getZHZT() {
        return ZHZT;
    }

    public void setZHZT(String ZHZT) {
        this.ZHZT = ZHZT;
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

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String DWMC) {
        this.DWMC = DWMC;
    }

    public String getDWZH() {
        return DWZH;
    }

    public void setDWZH(String DWZH) {
        this.DWZH = DWZH;
    }

    @Override
    public String toString() {
        return "PersonAccountsGet{" +
                "GRZH='" + GRZH + '\'' +
                ", XimgMing='" + XimgMing + '\'' +
                ", GRYHCKZHHM='" + GRYHCKZHHM + '\'' +
                ", GRYHCKKHYHMC='" + GRYHCKKHYHMC + '\'' +
                ", ZHZT='" + ZHZT + '\'' +
                ", ZHYE='" + ZHYE + '\'' +
                ", JZNY='" + JZNY + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", DWZH='" + DWZH + '\'' +
                '}';
    }
}