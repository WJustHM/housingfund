package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ListUnitDepositReviewedResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitDepositReviewedResRes implements Serializable {

    private String YWLSH;  //业务流水号

    private String SHZT;  //审核状态

    private String YWWD;  //办理网点

    private String SLSJ;  //受理时间

    private String YWMXLX;  //业务明细类型

    private String DWMC;  //单位名称

    private String DWZH;  //单位账号

    private String CZY;  //操作员

    private String YWZT;    //业务状态（STEP)

    private String DWLB;    //单位类别

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
        return "ListUnitDepositReviewedResRes{" +
                "YWLSH='" + YWLSH + '\'' +
                ", SHZT='" + SHZT + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                ", YWMXLX='" + YWMXLX + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", CZY='" + CZY + '\'' +
                ", YWZT='" + YWZT + '\'' +
                ", DWLB='" + DWLB + '\'' +
                ", SCSHY='" + SCSHY + '\'' +
                ", id='" + id + '\'' +
                '}';
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


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getSLSJ() {

        return this.SLSJ;

    }


    public void setSLSJ(String SLSJ) {

        this.SLSJ = SLSJ;

    }


    public String getYWMXLX() {

        return this.YWMXLX;

    }


    public void setYWMXLX(String YWMXLX) {

        this.YWMXLX = YWMXLX;

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

    public String getYWZT() {
        return YWZT;
    }

    public void setYWZT(String YWZT) {
        this.YWZT = YWZT;
    }

    public String getDWLB() {
        return DWLB;
    }

    public void setDWLB(String DWLB) {
        this.DWLB = DWLB;
    }

    public String getSCSHY() {
        return SCSHY;
    }

    public void setSCSHY(String SCSHY) {
        this.SCSHY = SCSHY;
    }

}