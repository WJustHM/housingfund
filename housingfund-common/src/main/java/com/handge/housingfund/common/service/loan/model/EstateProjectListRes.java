package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "EstateProjectListRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstateProjectListRes  implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    private String BZJBL;  //保证金比例

    private String LPMC;  //楼盘名称

    private String LPBH;  //楼盘编号

    private String YWLSH; //业务流水号

    private String FKGS;  //房开公司

    private String FKGSZH;//房开公司账号

    private String LXR;//联系人

    private String LXDH;//联系电话

    private String ZHUANGTAI;//状态

    private String YWWD;  //业务网点

    private String LPDZ;  //楼盘地址

    private String CZY;  //操作员

    private String SFQY;  //是否启用

    private String SLSJ;  //受理时间

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getSFQY() {
        return SFQY;
    }

    public void setSFQY(String SFQY) {
        this.SFQY = SFQY;
    }

    public String getBZJBL() {

        return this.BZJBL;

    }


    public void setBZJBL(String BZJBL) {

        this.BZJBL = BZJBL;

    }

    public String getFKGSZH() {
        return FKGSZH;
    }

    public void setFKGSZH(String FKGSZH) {
        this.FKGSZH = FKGSZH;
    }

    public String getLXR() {
        return LXR;
    }

    public void setLXR(String LXR) {
        this.LXR = LXR;
    }

    public String getLXDH() {
        return LXDH;
    }

    public void setLXDH(String LXDH) {
        this.LXDH = LXDH;
    }

    public String getZHUANGTAI() {
        return ZHUANGTAI;
    }

    public void setZHUANGTAI(String ZHUANGTAI) {
        this.ZHUANGTAI = ZHUANGTAI;
    }


    public String getLPMC() {

        return this.LPMC;

    }


    public void setLPMC(String LPMC) {

        this.LPMC = LPMC;

    }


    public String getLPBH() {

        return this.LPBH;

    }


    public void setLPBH(String LPBH) {

        this.LPBH = LPBH;

    }


    public String getFKGS() {

        return this.FKGS;

    }


    public void setFKGS(String FKGS) {

        this.FKGS = FKGS;

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


    public String getCZY() {

        return this.CZY;

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
        return "EstateProjectListRes{" +
                "BZJBL='" + BZJBL + '\'' +
                ", LPMC='" + LPMC + '\'' +
                ", LPBH='" + LPBH + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", FKGS='" + FKGS + '\'' +
                ", FKGSZH='" + FKGSZH + '\'' +
                ", LXR='" + LXR + '\'' +
                ", LXDH='" + LXDH + '\'' +
                ", ZHUANGTAI='" + ZHUANGTAI + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", LPDZ='" + LPDZ + '\'' +
                ", CZY='" + CZY + '\'' +
                ", SFQY='" + SFQY + '\'' +
                ", SLSJ='" + SLSJ + '\'' +

                '}';
    }
}