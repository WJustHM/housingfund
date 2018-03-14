package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ConfirmPaymentResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfirmPaymentResponse  implements Serializable {

    private String DKFFRQ;  //贷款发放日期

    private String YWLSH;  //业务流水号

    private String FKYHMC;  //放款银行名称

    private String YWWD;  //业务网点

    private String JKHTBH;  //借款合同编号

    private String JKRZJHM;  //借款人证件号码

    private String SKZH;  //收款账号

    private String SKZHHM;  //收款账号户名

    private String FKZHHM;  //放款账户户名

    private String DKFFE;  //贷款发放额

    private String JKRXM;  //借款人姓名

    private String JKRZJLX;  //借款人证件类型

    private String SKYHMC;  //收款银行名称

    private String FKZH;  //放款账户

    private String DKZH;  //贷款账号

    private String CZY;  //操作员

    public String getDKFFRQ() {

        return this.DKFFRQ;

    }


    public void setDKFFRQ(String DKFFRQ) {

        this.DKFFRQ = DKFFRQ;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getFKYHMC() {

        return this.FKYHMC;

    }


    public void setFKYHMC(String FKYHMC) {

        this.FKYHMC = FKYHMC;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getJKHTBH() {

        return this.JKHTBH;

    }


    public void setJKHTBH(String JKHTBH) {

        this.JKHTBH = JKHTBH;

    }


    public String getJKRZJHM() {

        return this.JKRZJHM;

    }


    public void setJKRZJHM(String JKRZJHM) {

        this.JKRZJHM = JKRZJHM;

    }


    public String getSKZH() {

        return this.SKZH;

    }


    public void setSKZH(String SKZH) {

        this.SKZH = SKZH;

    }


    public String getSKZHHM() {

        return this.SKZHHM;

    }


    public void setSKZHHM(String SKZHHM) {

        this.SKZHHM = SKZHHM;

    }


    public String getFKZHHM() {

        return this.FKZHHM;

    }


    public void setFKZHHM(String FKZHHM) {

        this.FKZHHM = FKZHHM;

    }


    public String getDKFFE() {

        return this.DKFFE;

    }


    public void setDKFFE(String DKFFE) {

        this.DKFFE = DKFFE;

    }


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public String getJKRZJLX() {

        return this.JKRZJLX;

    }


    public void setJKRZJLX(String JKRZJLX) {

        this.JKRZJLX = JKRZJLX;

    }


    public String getSKYHMC() {

        return this.SKYHMC;

    }


    public void setSKYHMC(String SKYHMC) {

        this.SKYHMC = SKYHMC;

    }


    public String getFKZH() {

        return this.FKZH;

    }


    public void setFKZH(String FKZH) {

        this.FKZH = FKZH;

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


    public String toString() {

        return "ConfirmPaymentResponse{" +

                "DKFFRQ='" + this.DKFFRQ + '\'' + "," +
                "YWLSH='" + this.YWLSH + '\'' + "," +
                "FKYHMC='" + this.FKYHMC + '\'' + "," +
                "YWWD='" + this.YWWD + '\'' + "," +
                "JKHTBH='" + this.JKHTBH + '\'' + "," +
                "JKRZJHM='" + this.JKRZJHM + '\'' + "," +
                "SKZH='" + this.SKZH + '\'' + "," +
                "SKZHHM='" + this.SKZHHM + '\'' + "," +
                "FKZHHM='" + this.FKZHHM + '\'' + "," +
                "DKFFE='" + this.DKFFE + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' + "," +
                "JKRZJLX='" + this.JKRZJLX + '\'' + "," +
                "SKYHMC='" + this.SKYHMC + '\'' + "," +
                "FKZH='" + this.FKZH + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' + "," +
                "CZY='" + this.CZY + '\'' +

                "}";

    }
}