package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HousingCompaniesListRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingCompaniesListRes  implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    private String DWDZ;  //单位地址

    private String LXR;  //联系人

    private String FKGS;  //房开公司

    private String SFQY;  //是否启用

    private String FKZH;  //房开账号

    private String YWWD;  //业务网点

    private String LXDH;  //联系电话

    private String CZY;  //操作员

    private String ZZJGDM;//组织机构代码

    private String SLSJ;//受理时间

    public String getZZJGDM() {
        return ZZJGDM;
    }

    public void setZZJGDM(String ZZJGDM) {
        this.ZZJGDM = ZZJGDM;
    }

    public String getDWDZ() {

        return this.DWDZ;

    }


    public void setDWDZ(String DWDZ) {

        this.DWDZ = DWDZ;

    }


    public String getLXR() {

        return this.LXR;

    }


    public void setLXR(String LXR) {

        this.LXR = LXR;

    }


    public String getFKGS() {

        return this.FKGS;

    }


    public void setFKGS(String FKGS) {

        this.FKGS = FKGS;

    }



    public String getFKZH() {

        return this.FKZH;

    }


    public void setFKZH(String FKZH) {

        this.FKZH = FKZH;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getLXDH() {

        return this.LXDH;

    }


    public void setLXDH(String LXDH) {

        this.LXDH = LXDH;

    }


    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }

    public String getSFQY() {
        return SFQY;
    }

    public void setSFQY(String SFQY) {
        this.SFQY = SFQY;
    }

    public String getSLSJ() {
        return SLSJ;
    }

    public void setSLSJ(String SLSJ) {
        this.SLSJ = SLSJ;
    }

    @Override
    public String toString() {
        return "HousingCompaniesListRes{" +
                "DWDZ='" + DWDZ + '\'' +
                ", LXR='" + LXR + '\'' +
                ", FKGS='" + FKGS + '\'' +
                ", SFQY='" + SFQY + '\'' +
                ", FKZH='" + FKZH + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", LXDH='" + LXDH + '\'' +
                ", CZY='" + CZY + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                '}';
    }
}