package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "EstateProjectRecordsRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstateProjectRecordsRes  implements Serializable {

    private String LPMC;  //楼盘名称

    private String ZhuangTai;  //状态

    private String FKGS;  //房开公司

    private String LXR;  //联系人

    private String YWWD;  //业务网点

    private String LPDZ;  //楼盘地址

    private String LXDH;  //联系电话

    private String CZY;  //操作员

    public String getFKGS() {
        return FKGS;
    }

    public void setFKGS(String FKGS) {
        this.FKGS = FKGS;
    }

    public String getLPMC() {

        return this.LPMC;

    }


    public void setLPMC(String LPMC) {

        this.LPMC = LPMC;

    }


    public String getZhuangTai() {

        return this.ZhuangTai;

    }


    public void setZhuangTai(String ZhuangTai) {

        this.ZhuangTai = ZhuangTai;

    }


    public String getLXR() {

        return this.LXR;

    }


    public void setLXR(String LXR) {

        this.LXR = LXR;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getLPDZ() {

        return this.LPDZ;

    }


    public void setLPDZ(String LPDZ) {

        this.LPDZ = LPDZ;

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


    @Override
    public String toString() {
        return "EstateProjectRecordsRes{" +
                "LPMC='" + LPMC + '\'' +
                ", ZhuangTai='" + ZhuangTai + '\'' +
                ", FKGS='" + FKGS + '\'' +
                ", LXR='" + LXR + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", LPDZ='" + LPDZ + '\'' +
                ", LXDH='" + LXDH + '\'' +
                ", CZY='" + CZY + '\'' +
                '}';
    }
}