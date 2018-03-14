package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "LoanRecords")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanRecords  implements Serializable {

    private String HTDKJE;  //合同贷款金额

    private String JKHTH;  //借款合同号

    private String YWWD;  //业务网点

    private String DKYT;  //贷款用途（0：所有 1：购买 2：自建、翻修 3：大修）

    private String DKQS;  //贷款期数

    private String DKZH;  //贷款账号

    private String CZY;  //操作员

    private String JKRXM;  //借款人姓名

    public String getHTDKJE() {

        return this.HTDKJE;

    }


    public void setHTDKJE(String HTDKJE) {

        this.HTDKJE = HTDKJE;

    }


    public String getJKHTH() {

        return this.JKHTH;

    }


    public void setJKHTH(String JKHTH) {

        this.JKHTH = JKHTH;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getDKYT() {

        return this.DKYT;

    }


    public void setDKYT(String DKYT) {

        this.DKYT = DKYT;

    }


    public String getDKQS() {

        return this.DKQS;

    }


    public void setDKQS(String DKQS) {

        this.DKQS = DKQS;

    }


    public String getDKZH() {

        return this.DKZH;

    }


    public void setDKZH(String DKZH) {

        this.DKZH = DKZH;

    }


    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public String toString() {

        return "LoanRecords{" +

                "HTDKJE='" + this.HTDKJE + '\'' + "," +
                "JKHTH='" + this.JKHTH + '\'' + "," +
                "YWWD='" + this.YWWD + '\'' + "," +
                "DKYT='" + this.DKYT + '\'' + "," +
                "DKQS='" + this.DKQS + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' + "," +
                "CZY='" + this.CZY + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' +

                "}";

    }
}