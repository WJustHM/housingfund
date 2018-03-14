package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HousingCompanyRecordsRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingCompanyRecordsRes  implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    private String ZZJGDM;  //组织机构代码

    private String YWLSH;  //业务流水号

    private String DWDZ;  //单位地址

    private String ZhuangTai;  //状态

    private String FKGS;  //房开公司

    private String FKZH;  //房开账号

    private String YWWD;  //业务网点

    private String CZY;  //操作员

    private String SLSJ; //受理时间

    public String getZZJGDM() {
        return ZZJGDM;
    }

    public void setZZJGDM(String ZZJGDM) {
        this.ZZJGDM = ZZJGDM;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getDWDZ() {
        return DWDZ;
    }

    public void setDWDZ(String DWDZ) {
        this.DWDZ = DWDZ;
    }

    public String getZhuangTai() {
        return ZhuangTai;
    }

    public void setZhuangTai(String zhuangTai) {
        ZhuangTai = zhuangTai;
    }

    public String getFKGS() {
        return FKGS;
    }

    public void setFKGS(String FKGS) {
        this.FKGS = FKGS;
    }

    public String getFKZH() {
        return FKZH;
    }

    public void setFKZH(String FKZH) {
        this.FKZH = FKZH;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getSLSJ() {
        return SLSJ;
    }

    public void setSLSJ(String SLSJ) {
        this.SLSJ = SLSJ;
    }

    @Override
    public String toString() {
        return "HousingCompanyRecordsRes{" +
                "ZZJGDM='" + ZZJGDM + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", DWDZ='" + DWDZ + '\'' +
                ", ZhuangTai='" + ZhuangTai + '\'' +
                ", FKGS='" + FKGS + '\'' +
                ", FKZH='" + FKZH + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", CZY='" + CZY + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                '}';
    }
}