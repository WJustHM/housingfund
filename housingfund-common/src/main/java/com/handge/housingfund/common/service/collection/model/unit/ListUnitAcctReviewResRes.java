package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ListUnitAcctReviewResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitAcctReviewResRes implements Serializable {

    private String YWLSH;  //业务流水号

    private String SHZT;  //审核状态

    private String YWMXLX;  //业务明细类型

    private String DDSJ;  //到达时间

    private String YWWD;  //业务网点

    private String DWMC;  //单位名称

    private String DWZH;  //单位账号

    private String CZY;  //操作员

    private String DWLB;  //单位类别

    private String DQSHY;

    private String DQXM;

    private String SFTS;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ListUnitAcctReviewResRes{" +
                "YWLSH='" + YWLSH + '\'' +
                ", SHZT='" + SHZT + '\'' +
                ", YWMXLX='" + YWMXLX + '\'' +
                ", DDSJ='" + DDSJ + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", CZY='" + CZY + '\'' +
                ", DWLB='" + DWLB + '\'' +
                ", DQSHY='" + DQSHY + '\'' +
                ", DQXM='" + DQXM + '\'' +
                ", SFTS='" + SFTS + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getSFTS() {
        return SFTS;
    }

    public void setSFTS(String SFTS) {
        this.SFTS = SFTS;
    }

    public String getDQSHY() {
        return DQSHY;
    }

    public void setDQSHY(String DQSHY) {
        this.DQSHY = DQSHY;
    }

    public String getDQXM() {
        return DQXM;
    }

    public void setDQXM(String DQXM) {
        this.DQXM = DQXM;
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


    public String getDDSJ() {

        return this.DDSJ;

    }


    public void setDDSJ(String DDSJ) {

        this.DDSJ = DDSJ;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

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