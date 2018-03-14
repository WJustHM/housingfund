package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GuaranteeCompanyGetRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GuaranteeCompanyGetRes  implements Serializable {

    private String ZZJGDM;  //组织机构代码

    private String YWLSH;  //业务流水号

    private String FRDB;  //法人代表

    private String ZhuangTai;  //状态

    private String LXR;  //联系人

    private String YWWD;  //业务网点

    private String LXDH;  //联系电话

    private String CZY;  //操作员

    private String DBGS;  //担保公司

    private String XuHao;  //序号

    public String getZZJGDM() {

        return this.ZZJGDM;

    }


    public void setZZJGDM(String ZZJGDM) {

        this.ZZJGDM = ZZJGDM;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getFRDB() {

        return this.FRDB;

    }


    public void setFRDB(String FRDB) {

        this.FRDB = FRDB;

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


    public String getDBGS() {

        return this.DBGS;

    }


    public void setDBGS(String DBGS) {

        this.DBGS = DBGS;

    }


    public String getXuHao() {

        return this.XuHao;

    }


    public void setXuHao(String XuHao) {

        this.XuHao = XuHao;

    }


    public String toString() {

        return "GuaranteeCompanyGetRes{" +

                "ZZJGDM='" + this.ZZJGDM + '\'' + "," +
                "YWLSH='" + this.YWLSH + '\'' + "," +
                "FRDB='" + this.FRDB + '\'' + "," +
                "ZhuangTai='" + this.ZhuangTai + '\'' + "," +
                "LXR='" + this.LXR + '\'' + "," +
                "YWWD='" + this.YWWD + '\'' + "," +
                "LXDH='" + this.LXDH + '\'' + "," +
                "CZY='" + this.CZY + '\'' + "," +
                "DBGS='" + this.DBGS + '\'' + "," +
                "XuHao='" + this.XuHao + '\'' +

                "}";

    }
}