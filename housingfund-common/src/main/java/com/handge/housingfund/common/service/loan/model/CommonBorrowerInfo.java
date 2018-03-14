package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/8/16.
 */
@XmlRootElement(name = "CommonBorrowerInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommonBorrowerInfo implements Serializable {

    private static final long serialVersionUID = 1976830699877189990L;
    private String  GTJKRXM  ;
    private String  GTJKRZJLX;
    private String  GTJKRZJHM;
    private String  GDDHHM;
    private String  SJHM;
    private String YSR;
    private String  HKSZD;

    public String getGTJKRXM() {
        return GTJKRXM;
    }

    public void setGTJKRXM(String GTJKRXM) {
        this.GTJKRXM = GTJKRXM;
    }

    public String getGTJKRZJLX() {
        return GTJKRZJLX;
    }

    public void setGTJKRZJLX(String GTJKRZJLX) {
        this.GTJKRZJLX = GTJKRZJLX;
    }

    public String getGTJKRZJHM() {
        return GTJKRZJHM;
    }

    public void setGTJKRZJHM(String GTJKRZJHM) {
        this.GTJKRZJHM = GTJKRZJHM;
    }

    public String getGDDHHM() {
        return GDDHHM;
    }

    public void setGDDHHM(String GDDHHM) {
        this.GDDHHM = GDDHHM;
    }

    public String getSJHM() {
        return SJHM;
    }

    public void setSJHM(String SJHM) {
        this.SJHM = SJHM;
    }

    public String getYSR() {
        return YSR;
    }

    public void setYSR(String YSR) {
        this.YSR = YSR;
    }

    public String getHKSZD() {
        return HKSZD;
    }

    public void setHKSZD(String HKSZD) {
        this.HKSZD = HKSZD;
    }

    @Override
    public String toString() {
        return "CommonBorrowerInfo{" +
                "GTJKRXM='" + GTJKRXM + '\'' +
                ", GTJKRZJLX='" + GTJKRZJLX + '\'' +
                ", GTJKRZJHM='" + GTJKRZJHM + '\'' +
                ", GDDHHM='" + GDDHHM + '\'' +
                ", SJHM='" + SJHM + '\'' +
                ", YSR='" + YSR + '\'' +
                ", HKSZD='" + HKSZD + '\'' +
                '}';
    }
}
