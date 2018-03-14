package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/8/16.
 */
@XmlRootElement(name = "CommonProvidentFund")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommonProvidentFund implements Serializable {

    private static final long serialVersionUID = 9024331869687640993L;
    private String JZNY;
    private String GRZHZT;
    private String LXZCJCYS;
    private String YJCE;
    private String GRJCJS;
    private String GRZHYE;

    public String getJZNY() {
        return JZNY;
    }

    public void setJZNY(String JZNY) {
        this.JZNY = JZNY;
    }

    public String getGRZHZT() {
        return GRZHZT;
    }

    public void setGRZHZT(String GRZHZT) {
        this.GRZHZT = GRZHZT;
    }

    public String getLXZCJCYS() {
        return LXZCJCYS;
    }

    public void setLXZCJCYS(String LXZCJCYS) {
        this.LXZCJCYS = LXZCJCYS;
    }

    public String getYJCE() {
        return YJCE;
    }

    public void setYJCE(String YJCE) {
        this.YJCE = YJCE;
    }

    public String getGRJCJS() {
        return GRJCJS;
    }

    public void setGRJCJS(String GRJCJS) {
        this.GRJCJS = GRJCJS;
    }

    public String getGRZHYE() {
        return GRZHYE;
    }

    public void setGRZHYE(String GRZHYE) {
        this.GRZHYE = GRZHYE;
    }

    @Override
    public String toString() {
        return "ProvidentFundAccountInfo{" +
                "JZNY='" + JZNY + '\'' +
                ", GRZHZT='" + GRZHZT + '\'' +
                ", LXZCJCYS='" + LXZCJCYS + '\'' +
                ", YJCE='" + YJCE + '\'' +
                ", GRJCJS='" + GRJCJS + '\'' +
                ", GRZHYE='" + GRZHYE + '\'' +
                '}';
    }
}