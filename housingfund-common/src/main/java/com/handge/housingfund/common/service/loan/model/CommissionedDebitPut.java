package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "CommissionedDebitPut")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommissionedDebitPut  implements Serializable {

    private String WTKHSQRGJJZH;  //委托扣划申请人公积金账号

//   private      Information;  //

    private String ZJWTKHSQR;  //增加委托扣划申请人（0：是 1：否）

    private String ZJHM;  //证件号码

    private String WD_id;  //网点id

    private String HKZH;  //还款账号

    private String YDDH;  //移动电话

    private  String    BLZL;   //办理资料  // 办理资料

    private String WTKHSQRXM;  //委托扣划申请人姓名

    private String GZDW;  //工作单位

    private String YJKRGX;  //与借款人关系（0：本人 1：配偶）

    private String WTKHFS;  //委托扣划方式

    public String getWTKHSQRGJJZH() {

        return this.WTKHSQRGJJZH;

    }


    public void setWTKHSQRGJJZH(String WTKHSQRGJJZH) {

        this.WTKHSQRGJJZH = WTKHSQRGJJZH;

    }


    public String getZJWTKHSQR() {

        return this.ZJWTKHSQR;

    }


    public void setZJWTKHSQR(String ZJWTKHSQR) {

        this.ZJWTKHSQR = ZJWTKHSQR;

    }


    public String getZJHM() {

        return this.ZJHM;

    }


    public void setZJHM(String ZJHM) {

        this.ZJHM = ZJHM;

    }


    public String getWD_id() {

        return this.WD_id;

    }


    public void setWD_id(String WD_id) {

        this.WD_id = WD_id;

    }


    public String getHKZH() {

        return this.HKZH;

    }


    public void setHKZH(String HKZH) {

        this.HKZH = HKZH;

    }


    public String getYDDH() {

        return this.YDDH;

    }


    public void setYDDH(String YDDH) {

        this.YDDH = YDDH;

    }


    public String getSCZL() {

        return this.BLZL;

    }


    public void setSCZL(String SCZL) {

        this.BLZL = SCZL;

    }


    public String getWTKHSQRXM() {

        return this.WTKHSQRXM;

    }


    public void setWTKHSQRXM(String WTKHSQRXM) {

        this.WTKHSQRXM = WTKHSQRXM;

    }


    public String getGZDW() {

        return this.GZDW;

    }


    public void setGZDW(String GZDW) {

        this.GZDW = GZDW;

    }


    public String getYJKRGX() {

        return this.YJKRGX;

    }


    public void setYJKRGX(String YJKRGX) {

        this.YJKRGX = YJKRGX;

    }


    public String getWTKHFS() {

        return this.WTKHFS;

    }


    public void setWTKHFS(String WTKHFS) {

        this.WTKHFS = WTKHFS;

    }


    public String toString() {

        return "CommissionedDebitPut{" +

                "WTKHSQRGJJZH='" + this.WTKHSQRGJJZH + '\'' + "," +
                "ZJWTKHSQR='" + this.ZJWTKHSQR + '\'' + "," +
                "ZJHM='" + this.ZJHM + '\'' + "," +
                "WD_id='" + this.WD_id + '\'' + "," +
                "HKZH='" + this.HKZH + '\'' + "," +
                "YDDH='" + this.YDDH + '\'' + "," +
                "BLZL='" + this.BLZL + '\'' + "," +
                "WTKHSQRXM='" + this.WTKHSQRXM + '\'' + "," +
                "GZDW='" + this.GZDW + '\'' + "," +
                "YJKRGX='" + this.YJKRGX + '\'' + "," +
                "WTKHFS='" + this.WTKHFS + '\'' +

                "}";

    }
}