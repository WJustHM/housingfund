package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ListReviewIndiAcctResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListReviewIndiAcctResRes implements Serializable {

    private String YWLSH;  //业务流水号

    private String ZJHM;  //证件号码

    private String DDSJ;  //到达时间

    private String YWMXLX;  //业务明细类型

    private String YWWD;  //业务网点

    private String GRZH;  //个人账号

    private String XingMing;  //姓名

    private String CZY;  //操作员

    private String DWMC;    //单位名称

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
        return "ListReviewIndiAcctResRes{" +
                "YWLSH='" + YWLSH + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                ", DDSJ='" + DDSJ + '\'' +
                ", YWMXLX='" + YWMXLX + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", GRZH='" + GRZH + '\'' +
                ", XingMing='" + XingMing + '\'' +
                ", CZY='" + CZY + '\'' +
                ", DWMC='" + DWMC + '\'' +
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

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String DWMC) {
        this.DWMC = DWMC;
    }

    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getZJHM() {

        return this.ZJHM;

    }


    public void setZJHM(String ZJHM) {

        this.ZJHM = ZJHM;

    }


    public String getDDSJ() {

        return this.DDSJ;

    }


    public void setDDSJ(String DDSJ) {

        this.DDSJ = DDSJ;

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


    public String getGRZH() {

        return this.GRZH;

    }


    public void setGRZH(String GRZH) {

        this.GRZH = GRZH;

    }


    public String getXingMing() {

        return this.XingMing;

    }


    public void setXingMing(String XingMing) {

        this.XingMing = XingMing;

    }


    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }

}