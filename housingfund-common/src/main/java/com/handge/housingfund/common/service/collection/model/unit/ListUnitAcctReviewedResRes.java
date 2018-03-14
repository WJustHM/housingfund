package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ListUnitAcctReviewedResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitAcctReviewedResRes implements Serializable {

    private String SLSJ;  //受理时间

    private String YWLSH;  //业务流水号

    private String SHZT;  //审核状态

    private String YWMXLX;  //业务明细类型

    private String YWWD;  //业务网点

    private String YWZT;  //业务状态

    private String DWMC;  //单位名称

    private String DWZH;  //单位账号

    private String CZY;  //操作员

    private String DWLB;  //单位经济类型

    private String SCSHY;   //上次审核员

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ListUnitAcctReviewedResRes{" +
                "SLSJ='" + SLSJ + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", SHZT='" + SHZT + '\'' +
                ", YWMXLX='" + YWMXLX + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", YWZT='" + YWZT + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", CZY='" + CZY + '\'' +
                ", DWLB='" + DWLB + '\'' +
                ", SCSHY='" + SCSHY + '\'' +
                ", id='" + id + '\'' +
                '}';
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


    public String getSHZT() {

        return this.SHZT;

    }


    public void setSHZT(String SHZT) {

        this.SHZT = SHZT;

    }


    public String getYWMXLX() {

        return this.YWMXLX;

    }


    public void setYWMXLX(String YWMXLX) {

        this.YWMXLX = YWMXLX;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getYWZT() {

        return this.YWZT;

    }


    public void setYWZT(String YWZT) {

        this.YWZT = YWZT;

    }


    public String getDWMC() {

        return this.DWMC;

    }


    public void setDWMC(String DWMC) {

        this.DWMC = DWMC;

    }


    public String getDWZH() {

        return this.DWZH;

    }


    public void setDWZH(String DWZH) {

        this.DWZH = DWZH;

    }


    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }


    public String getDWLB() {

        return this.DWLB;

    }


    public void setDWLB(String DWLB) {

        this.DWLB = DWLB;

    }


}