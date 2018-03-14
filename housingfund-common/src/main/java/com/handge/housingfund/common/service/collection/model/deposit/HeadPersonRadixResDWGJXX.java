package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HeadPersonRadixResDWGJXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadPersonRadixResDWGJXX implements Serializable {

    private static final long serialVersionUID = -657917283500689771L;
    private String JBRZJLX;  //经办人证件类型

    private String YWLSH;  //业务流水号

    private String DWJBR;  //单位经办人

    private String TZSJ;  //填制时间

    private String YZM;  //验证码

    private String JBRZJHM;  //经办人证件号码

    private String DWMC;  //单位名称

    private String DWZH;  //单位账号

    private String CZY;  //操作员

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    private String YWWD; //业务网点

    private String FSRS;

    public String getFSRS() {
        return FSRS;
    }

    public void setFSRS(String FSRS) {
        this.FSRS = FSRS;
    }

    public String getJBRZJLX() {

        return this.JBRZJLX;

    }


    public void setJBRZJLX(String JBRZJLX) {

        this.JBRZJLX = JBRZJLX;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getDWJBR() {

        return this.DWJBR;

    }


    public void setDWJBR(String DWJBR) {

        this.DWJBR = DWJBR;

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


    public String getJBRZJHM() {

        return this.JBRZJHM;

    }


    public void setJBRZJHM(String JBRZJHM) {

        this.JBRZJHM = JBRZJHM;

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


    public String toString() {

        return "HeadPersonRadixResDWGJXX{" +

            "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
            "YWLSH='" + this.YWLSH + '\'' + "," +
            "DWJBR='" + this.DWJBR + '\'' + "," +
            "TZSJ='" + this.TZSJ + '\'' + "," +
            "YZM='" + this.YZM + '\'' + "," +
            "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
            "DWMC='" + this.DWMC + '\'' + "," +
            "DWZH='" + this.DWZH + '\'' + "," +
            "CZY='" + this.CZY + '\'' +
            "YWWD='" + this.YWWD + '\'' +
            "}";

    }
}