package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "BorrowerChargePostBGHGTJKRXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class BorrowerChargePostBGHGTJKRXX  implements Serializable {

    private String CDGX;  //参贷关系

    private String HKSZD;  //户口所在地

    private String GTJKRGJJZH;  //共同借款人公积金账号

    private String SJHM;  //手机号码

    private String GDDHHM;  //固定电话号码

    private String JCD;  //缴存地

    private String GTJKRZJHM;  //共同借款人证件号码

    private String GTJKRZJLX;  //共同借款人证件类型

    private String YSR;  //月收入

    private String GTJKRXM;  //共同借款人姓名

    public String getCDGX() {

        return this.CDGX;

    }


    public void setCDGX(String CDGX) {

        this.CDGX = CDGX;

    }


    public String getHKSZD() {

        return this.HKSZD;

    }


    public void setHKSZD(String HKSZD) {

        this.HKSZD = HKSZD;

    }


    public String getGTJKRGJJZH() {

        return this.GTJKRGJJZH;

    }


    public void setGTJKRGJJZH(String GTJKRGJJZH) {

        this.GTJKRGJJZH = GTJKRGJJZH;

    }


    public String getSJHM() {

        return this.SJHM;

    }


    public void setSJHM(String SJHM) {

        this.SJHM = SJHM;

    }


    public String getGDDHHM() {

        return this.GDDHHM;

    }


    public void setGDDHHM(String GDDHHM) {

        this.GDDHHM = GDDHHM;

    }


    public String getJCD() {

        return this.JCD;

    }


    public void setJCD(String JCD) {

        this.JCD = JCD;

    }


    public String getGTJKRZJHM() {

        return this.GTJKRZJHM;

    }


    public void setGTJKRZJHM(String GTJKRZJHM) {

        this.GTJKRZJHM = GTJKRZJHM;

    }


    public String getGTJKRZJLX() {

        return this.GTJKRZJLX;

    }


    public void setGTJKRZJLX(String GTJKRZJLX) {

        this.GTJKRZJLX = GTJKRZJLX;

    }


    public String getYSR() {

        return this.YSR;

    }


    public void setYSR(String YSR) {

        this.YSR = YSR;

    }


    public String getGTJKRXM() {

        return this.GTJKRXM;

    }


    public void setGTJKRXM(String GTJKRXM) {

        this.GTJKRXM = GTJKRXM;

    }


    public String toString() {

        return "BorrowerChargePostBGHGTJKRXX{" +

                "CDGX='" + this.CDGX + '\'' + "," +
                "HKSZD='" + this.HKSZD + '\'' + "," +
                "GTJKRGJJZH='" + this.GTJKRGJJZH + '\'' + "," +
                "SJHM='" + this.SJHM + '\'' + "," +
                "GDDHHM='" + this.GDDHHM + '\'' + "," +
                "JCD='" + this.JCD + '\'' + "," +
                "GTJKRZJHM='" + this.GTJKRZJHM + '\'' + "," +
                "GTJKRZJLX='" + this.GTJKRZJLX + '\'' + "," +
                "YSR='" + this.YSR + '\'' + "," +
                "GTJKRXM='" + this.GTJKRXM + '\'' +

                "}";

    }
}