package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ListIndiAcctReviewedResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListIndiAcctReviewedResRes implements Serializable {

    private String SLSJ;  //受理时间

    private String YWLSH;  //业务流水号

    private String ZhuangTai;  //状态

    private String YWWD;  //业务网点

    private String YWMXLX;  //业务明细类型

    private String GRZH;  //个人账号

    private String XingMing; //姓名

    private String CZY;  //操作员

    private String SCSHY;   //上次审核员

    private String DWMC;    //单位名称

    private String id;

    private String ZJHM;

    @Override
    public String toString() {
        return "ListIndiAcctReviewedResRes{" +
                "SLSJ='" + SLSJ + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", ZhuangTai='" + ZhuangTai + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", YWMXLX='" + YWMXLX + '\'' +
                ", GRZH='" + GRZH + '\'' +
                ", XingMing='" + XingMing + '\'' +
                ", CZY='" + CZY + '\'' +
                ", SCSHY='" + SCSHY + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", id='" + id + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                '}';
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String DWMC) {
        this.DWMC = DWMC;
    }

    public String getSCSHY() {
        return SCSHY;
    }

    public void setSCSHY(String SCSHY) {
        this.SCSHY = SCSHY;
    }

    public String getSLSJ() {

        return this.SLSJ;

    }


    public void setSLSJ(String SLSJ) {

        this.SLSJ = SLSJ;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getZhuangTai() {

        return this.ZhuangTai;

    }


    public void setZhuangTai(String ZhuangTai) {

        this.ZhuangTai = ZhuangTai;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getYWMXLX() {

        return this.YWMXLX;

    }


    public void setYWMXLX(String YWMXLX) {

        this.YWMXLX = YWMXLX;

    }


    public String getGRZH() {

        return this.GRZH;

    }


    public void setGRZH(String GRZH) {

        this.GRZH = GRZH;

    }


    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }

    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

}