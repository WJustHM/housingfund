package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HeadUnitPayCallReceiptRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitPayCallReceiptRes implements Serializable {

    private ArrayList<HeadUnitPayCallReceiptResCJJL> CJJL;  //催缴记录

    private String JBRXM;  //经办人姓名

    private String YWLSH;  //业务流水号

    private String TZSJ;  //填制时间

    private String YZM;  //验证码

    private String JBRGDDHHM;  //经办人固定电话号码

    private String DWMC;  //单位名称

    private String DWZH;  //单位账号

    private String CZY;  //操作员

    private String JBRSJHM;  //经办人手机号码

    private String YWWD; // 业务网点

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }


    public ArrayList<HeadUnitPayCallReceiptResCJJL> getCJJL() {

        return this.CJJL;

    }


    public void setCJJL(ArrayList<HeadUnitPayCallReceiptResCJJL> CJJL) {

        this.CJJL = CJJL;

    }


    public String getJBRXM() {

        return this.JBRXM;

    }


    public void setJBRXM(String JBRXM) {

        this.JBRXM = JBRXM;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getTZSJ() {

        return this.TZSJ;

    }


    public void setTZSJ(String TZSJ) {

        this.TZSJ = TZSJ;

    }


    public String getYZM() {

        return this.YZM;

    }


    public void setYZM(String YZM) {

        this.YZM = YZM;

    }


    public String getJBRGDDHHM() {

        return this.JBRGDDHHM;

    }


    public void setJBRGDDHHM(String JBRGDDHHM) {

        this.JBRGDDHHM = JBRGDDHHM;

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


    public String getJBRSJHM() {

        return this.JBRSJHM;

    }


    public void setJBRSJHM(String JBRSJHM) {

        this.JBRSJHM = JBRSJHM;

    }


    public String toString() {

        return "HeadUnitPayCallReceiptRes{" +

            "CJJL='" + this.CJJL + '\'' + "," +
            "JBRXM='" + this.JBRXM + '\'' + "," +
            "YWLSH='" + this.YWLSH + '\'' + "," +
            "TZSJ='" + this.TZSJ + '\'' + "," +
            "YZM='" + this.YZM + '\'' + "," +
            "JBRGDDHHM='" + this.JBRGDDHHM + '\'' + "," +
            "DWMC='" + this.DWMC + '\'' + "," +
            "DWZH='" + this.DWZH + '\'' + "," +
            "CZY='" + this.CZY + '\'' + "," +
            "JBRSJHM='" + this.JBRSJHM + '\'' +
            "YWWD='" + this.YWWD + '\'' +
            "}";

    }
}