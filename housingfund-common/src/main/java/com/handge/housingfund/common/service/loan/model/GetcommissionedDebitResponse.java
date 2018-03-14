package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "GetcommissionedDebitResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetcommissionedDebitResponse  implements Serializable {

    private String WTKHSQRGJJZH;  //委托扣划申请人公积金账号

//   private      Information;  //

    private String YWLSH;  //业务流水号

    private ArrayList<GetcommissionedDebitResponseManagerInformation> managerInformation;  //经办人信息

    private String JKRZJHM;  //借款人证件号码

    private String DKQS;  //贷款期数

    private String GZDW;  //工作单位

    private String YDDH;  //移动电话

    private String WTKHFS;  //委托扣划方式

    private String JKRXM;  //借款人姓名

    private String ZJWTKHSQR;  //增加委托扣划申请人（0：是 1：否）

    private String HTDKJE;  //合同贷款金额

    private String ZJHM;  //证件号码

    private String HKZH;  //还款账号

    private String WTKHSQRXM;  //委托扣划申请人姓名

    private  String    BLZL;   //办理资料  // 办理资料

    private String DKZH;  //贷款账号

    private String YJKRGX;  //与借款人关系（0：本人 1：配偶）

    public String getWTKHSQRGJJZH() {

        return this.WTKHSQRGJJZH;

    }


    public void setWTKHSQRGJJZH(String WTKHSQRGJJZH) {

        this.WTKHSQRGJJZH = WTKHSQRGJJZH;

    }

    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public ArrayList<GetcommissionedDebitResponseManagerInformation> getmanagerInformation() {

        return this.managerInformation;

    }


    public void setmanagerInformation(ArrayList<GetcommissionedDebitResponseManagerInformation> managerInformation) {

        this.managerInformation = managerInformation;

    }


    public String getJKRZJHM() {

        return this.JKRZJHM;

    }


    public void setJKRZJHM(String JKRZJHM) {

        this.JKRZJHM = JKRZJHM;

    }


    public String getDKQS() {

        return this.DKQS;

    }


    public void setDKQS(String DKQS) {

        this.DKQS = DKQS;

    }


    public String getGZDW() {

        return this.GZDW;

    }


    public void setGZDW(String GZDW) {

        this.GZDW = GZDW;

    }


    public String getYDDH() {

        return this.YDDH;

    }


    public void setYDDH(String YDDH) {

        this.YDDH = YDDH;

    }


    public String getWTKHFS() {

        return this.WTKHFS;

    }


    public void setWTKHFS(String WTKHFS) {

        this.WTKHFS = WTKHFS;

    }


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public String getZJWTKHSQR() {

        return this.ZJWTKHSQR;

    }


    public void setZJWTKHSQR(String ZJWTKHSQR) {

        this.ZJWTKHSQR = ZJWTKHSQR;

    }


    public String getHTDKJE() {

        return this.HTDKJE;

    }


    public void setHTDKJE(String HTDKJE) {

        this.HTDKJE = HTDKJE;

    }


    public String getZJHM() {

        return this.ZJHM;

    }


    public void setZJHM(String ZJHM) {

        this.ZJHM = ZJHM;

    }


    public String getHKZH() {

        return this.HKZH;

    }


    public void setHKZH(String HKZH) {

        this.HKZH = HKZH;

    }


    public String getWTKHSQRXM() {

        return this.WTKHSQRXM;

    }


    public void setWTKHSQRXM(String WTKHSQRXM) {

        this.WTKHSQRXM = WTKHSQRXM;

    }


    public String getSCZL() {

        return this.BLZL;

    }


    public void setSCZL(String SCZL) {

        this.BLZL = SCZL;

    }


    public String getDKZH() {

        return this.DKZH;

    }


    public void setDKZH(String DKZH) {

        this.DKZH = DKZH;

    }


    public String getYJKRGX() {

        return this.YJKRGX;

    }


    public void setYJKRGX(String YJKRGX) {

        this.YJKRGX = YJKRGX;

    }


    public String toString() {

        return "GetcommissionedDebitResponse{" +

                "WTKHSQRGJJZH='" + this.WTKHSQRGJJZH + '\'' + "," +
                "YWLSH='" + this.YWLSH + '\'' + "," +
                "managerInformation='" + this.managerInformation + '\'' + "," +
                "JKRZJHM='" + this.JKRZJHM + '\'' + "," +
                "DKQS='" + this.DKQS + '\'' + "," +
                "GZDW='" + this.GZDW + '\'' + "," +
                "YDDH='" + this.YDDH + '\'' + "," +
                "WTKHFS='" + this.WTKHFS + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' + "," +
                "ZJWTKHSQR='" + this.ZJWTKHSQR + '\'' + "," +
                "HTDKJE='" + this.HTDKJE + '\'' + "," +
                "ZJHM='" + this.ZJHM + '\'' + "," +
                "HKZH='" + this.HKZH + '\'' + "," +
                "WTKHSQRXM='" + this.WTKHSQRXM + '\'' + "," +
                "BLZL='" + this.BLZL + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' + "," +
                "YJKRGX='" + this.YJKRGX + '\'' +

                "}";

    }
}