package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/8/16.
 */
@XmlRootElement(name = "UnitInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class OthersInfo implements Serializable {

    private static final long serialVersionUID = 5914219822922711993L;
    private String  HKZH;
    private String  ZHKHYHMC;
    private String  WTKHYJCE;
    private String  HKZHHM;

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

    public String getWTKHYJCE() {
        return WTKHYJCE;
    }

    public void setWTKHYJCE(String WTKHYJCE) {
        this.WTKHYJCE = WTKHYJCE;
    }

    public String getHKZHHM() {
        return HKZHHM;
    }

    public void setHKZHHM(String HKZHHM) {
        this.HKZHHM = HKZHHM;
    }

    @Override
    public String toString() {
        return "OthersInfo{" +
                "HKZH='" + HKZH + '\'' +
                ", ZHKHYHMC='" + ZHKHYHMC + '\'' +
                ", WTKHYJCE='" + WTKHYJCE + '\'' +
                '}';
    }
}
