package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "SignContractResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignContractResponse  implements Serializable {

    private static final long serialVersionUID = 6011226985701938790L;
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

    private String FWZJ;  //房屋总价/计划费用

    private String JKRZJHM;  //借款人证件号码

    private String DKYT;  //贷款用途

    private String DKQS;  //贷款期数

    private String JKRXM;  //借款人姓名

    private String SLSJ;    //受理时间

    private String FFDKYH;//银行名称

    private String YWWD;//业务网点

    private String SBYY;

    private String CZY;

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getSBYY() {
        return SBYY;
    }

    public void setSBYY(String SBYY) {
        this.SBYY = SBYY;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getFFDKYH() {
        return FFDKYH;
    }

    public void setFFDKYH(String FFDKYH) {
        this.FFDKYH = FFDKYH;
    }

    @Override
    public String toString() {
        return "SignContractResponse{" +
                "YWLSH='" + YWLSH + '\'' +
                ", HTDKJE='" + HTDKJE + '\'' +
                ", ZhuangTai='" + ZhuangTai + '\'' +
                ", FWZJ='" + FWZJ + '\'' +
                ", JKRZJHM='" + JKRZJHM + '\'' +
                ", DKYT='" + DKYT + '\'' +
                ", DKQS='" + DKQS + '\'' +
                ", JKRXM='" + JKRXM + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                '}';
    }

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


    public String getFWZJ() {

        return this.FWZJ;

    }


    public void setFWZJ(String FWZJ) {

        this.FWZJ = FWZJ;

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


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


}