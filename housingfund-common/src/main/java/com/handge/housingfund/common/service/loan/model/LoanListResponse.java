package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "LoanListResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanListResponse  implements Serializable {

    private static final long serialVersionUID = -5740300059592865330L;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String YWLSH;  //业务流水号

    private String HTDKJE;  //合同贷款金额

    private String ZhuangTai;  //状态

    private String YWWD;  //业务网点

    private String JKRZJHM;  //借款人证件号码

    private String DKYT;  //贷款用途

    private String DKQS;  //贷款期数

    private String CZY;  //操作员

    private String JKRXM;  //借款人姓名

    private String SLSJ;    //受理时间

    public String getSLSJ() {
        return SLSJ;
    }

    public void setSLSJ(String SLSJ) {
        this.SLSJ = SLSJ;
    }

    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getHTDKJE() {

        return this.HTDKJE;

    }


    public void setHTDKJE(String HTDKJE) {

        this.HTDKJE = HTDKJE;

    }


    public String getZhuangTai() {

        return this.ZhuangTai;

    }


    public void setZhuangTai(String ZhuangTai) {

        this.ZhuangTai = ZhuangTai;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getJKRZJHM() {

        return this.JKRZJHM;

    }


    public void setJKRZJHM(String JKRZJHM) {

        this.JKRZJHM = JKRZJHM;

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


    @Override
    public String toString() {
        return "LoanListResponse{" +
                "YWLSH='" + YWLSH + '\'' +
                ", HTDKJE='" + HTDKJE + '\'' +
                ", ZhuangTai='" + ZhuangTai + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", JKRZJHM='" + JKRZJHM + '\'' +
                ", DKYT='" + DKYT + '\'' +
                ", DKQS='" + DKQS + '\'' +
                ", CZY='" + CZY + '\'' +
                ", JKRXM='" + JKRXM + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                '}';
    }
}