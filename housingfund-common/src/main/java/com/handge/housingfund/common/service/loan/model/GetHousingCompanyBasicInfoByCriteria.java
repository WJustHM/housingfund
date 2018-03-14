package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by 向超 on 2017/8/14.
 */
@XmlRootElement(name = "GetHousingCompanyBasicInfoByCriteria")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetHousingCompanyBasicInfoByCriteria  implements Serializable {
    private String FKZH;//房开账号

    private String FKGS;//房开公司

    private String SJFLB;//售建房类别

    private String DWDZ;//单位地址

    private String YWWD;//业务网点

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getFKZH() {
        return FKZH;
    }

    public void setFKZH(String FKZH) {
        this.FKZH = FKZH;
    }

    public String getFKGS() {
        return FKGS;
    }

    public void setFKGS(String FKGS) {
        this.FKGS = FKGS;
    }

    public String getSJFLB() {
        return SJFLB;
    }

    public void setSJFLB(String SJFLB) {
        this.SJFLB = SJFLB;
    }

    public String getDWDZ() {
        return DWDZ;
    }

    public void setDWDZ(String DWDZ) {
        this.DWDZ = DWDZ;
    }

    @Override
    public String toString() {
        return "GetHousingCompanyBasicInfoByCriteria{" +
                "FKZH='" + FKZH + '\'' +
                ", FKGS='" + FKGS + '\'' +
                ", SJFLB='" + SJFLB + '\'' +
                ", DWDZ='" + DWDZ + '\'' +
                ", YWWD='" + YWWD + '\'' +
                '}';
    }
}
