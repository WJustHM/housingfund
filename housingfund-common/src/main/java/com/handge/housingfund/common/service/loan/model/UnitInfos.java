package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/8/16.
 */
@XmlRootElement(name = "UnitInfos")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitInfos implements Serializable {

    private static final long serialVersionUID = -1556747122465767669L;
    private String DWMC;
    private String DWZH;
    @Annotation.GYPhone(name="单位电话")
    private String DWDH;
    private String DWLB;
    private String DWDZ;

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

    public String getDWDH() {
        return DWDH;
    }

    public void setDWDH(String DWDH) {
        this.DWDH = DWDH;
    }

    public String getDWLB() {
        return DWLB;
    }

    public void setDWLB(String DWLB) {
        this.DWLB = DWLB;
    }

    public String getDWDZ() {
        return DWDZ;
    }

    public void setDWDZ(String DWDZ) {
        this.DWDZ = DWDZ;
    }

    @Override
    public String toString() {
        return "UnitInfos{" +
                "DWMC='" + DWMC + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", DWDH='" + DWDH + '\'' +
                ", DWLB='" + DWLB + '\'' +
                ", DWDZ='" + DWDZ + '\'' +
                '}';
    }
}
