package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Liujuhao on 2017/8/9.
 */

@XmlRootElement(name = "CoborrowerUnitInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class CoborrowerUnitInformation  implements Serializable {

    private String DWMC;

    private String DWZH;

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
        return "CoborrowerUnitInformation{" +
                "DWMC='" + DWMC + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", DWDH='" + DWDH + '\'' +
                ", DWLB='" + DWLB + '\'' +
                ", DWDZ='" + DWDZ + '\'' +
                '}';
    }
}
