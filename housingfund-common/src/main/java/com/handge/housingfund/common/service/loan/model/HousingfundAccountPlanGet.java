package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.LinkedList;


@XmlRootElement(name = "HousingfundAccountPlanGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingfundAccountPlanGet  implements Serializable {

    private String JKRXM;  //借款人姓名

    private LinkedList<HousingfundAccountPlanGetInformation> information;  //还款计划信息列表

    private String JKRZJHM;  //借款人证件号码

    private String DKQS;  //贷款期数

    private String DKZH;  //贷款账号

    private String DKFFE;  //贷款发放额

    private String DKHKFS;  //贷款还款方式

    private String YWWD;  //业务网点

    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public LinkedList<HousingfundAccountPlanGetInformation> getinformation() {

        return this.information;

    }


    public void setinformation(LinkedList<HousingfundAccountPlanGetInformation> information) {

        this.information = information;

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


    public String getDKZH() {

        return this.DKZH;

    }


    public void setDKZH(String DKZH) {

        this.DKZH = DKZH;

    }


    public String getDKFFE() {

        return this.DKFFE;

    }


    public void setDKFFE(String DKFFE) {

        this.DKFFE = DKFFE;

    }


    public String getDKHKFS() {

        return this.DKHKFS;

    }


    public void setDKHKFS(String DKHKFS) {

        this.DKHKFS = DKHKFS;

    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String toString() {

        return "HousingfundAccountPlanGet{" +

                "JKRXM='" + this.JKRXM + '\'' + "," +
                "information='" + this.information + '\'' + "," +
                "JKRZJHM='" + this.JKRZJHM + '\'' + "," +
                "DKQS='" + this.DKQS + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' + "," +
                "DKFFE='" + this.DKFFE + '\'' + "," +
                "DKHKFS='" + this.DKHKFS + '\'' +
                "YWWD='" + this.YWWD + '\'' +
                "}";

    }
}