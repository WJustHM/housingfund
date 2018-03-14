package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HousingfundAccountBusinessListGetInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingfundAccountBusinessListGetInformation  implements Serializable {

    private String YWLSH;  //业务流水号

    private String DKYWMXLX;  //贷款业务明细类型

    private String YQZZCBJJE;  //逾期转正常本金金额

    private String FSE;  //发生额

    private String Zhuangtai;  //状态

    private String LXJE;  //利息金额

    private String BJJE;  //本金金额

    private Integer DQQC;  //当期期次

    private String DKYHDM;  //贷款银行代码

    private String JZRQ;  //记账日期

    private String YWFSRQ;  //业务发生日期

    private String ZCZYQBJJE;  //正常转逾期本金金额

    private String FXJE;  //罚息金额

    private String SBYY;    //失败原因

    @Override
    public String toString() {
        return "HousingfundAccountBusinessListGetInformation{" +
                "YWLSH='" + YWLSH + '\'' +
                ", DKYWMXLX='" + DKYWMXLX + '\'' +
                ", YQZZCBJJE='" + YQZZCBJJE + '\'' +
                ", FSE='" + FSE + '\'' +
                ", Zhuangtai='" + Zhuangtai + '\'' +
                ", LXJE='" + LXJE + '\'' +
                ", BJJE='" + BJJE + '\'' +
                ", DQQC=" + DQQC +
                ", DKYHDM='" + DKYHDM + '\'' +
                ", JZRQ='" + JZRQ + '\'' +
                ", YWFSRQ='" + YWFSRQ + '\'' +
                ", ZCZYQBJJE='" + ZCZYQBJJE + '\'' +
                ", FXJE='" + FXJE + '\'' +
                ", SBYY='" + SBYY + '\'' +
                '}';
    }

    public String getSBYY() {
        return SBYY;
    }

    public void setSBYY(String SBYY) {
        this.SBYY = SBYY;
    }

    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getDKYWMXLX() {

        return this.DKYWMXLX;

    }


    public void setDKYWMXLX(String DKYWMXLX) {

        this.DKYWMXLX = DKYWMXLX;

    }


    public String getYQZZCBJJE() {

        return this.YQZZCBJJE;

    }


    public void setYQZZCBJJE(String YQZZCBJJE) {

        this.YQZZCBJJE = YQZZCBJJE;

    }


    public String getFSE() {

        return this.FSE;

    }


    public void setFSE(String FSE) {

        this.FSE = FSE;

    }


    public String getZhuangtai() {

        return this.Zhuangtai;

    }


    public void setZhuangtai(String Zhuangtai) {

        this.Zhuangtai = Zhuangtai;

    }


    public String getLXJE() {

        return this.LXJE;

    }


    public void setLXJE(String LXJE) {

        this.LXJE = LXJE;

    }


    public String getBJJE() {

        return this.BJJE;

    }


    public void setBJJE(String BJJE) {

        this.BJJE = BJJE;

    }


    public Integer getDQQC() {

        return this.DQQC;

    }


    public void setDQQC(Integer DQQC) {

        this.DQQC = DQQC;

    }


    public String getDKYHDM() {

        return this.DKYHDM;

    }


    public void setDKYHDM(String DKYHDM) {

        this.DKYHDM = DKYHDM;

    }


    public String getJZRQ() {

        return this.JZRQ;

    }


    public void setJZRQ(String JZRQ) {

        this.JZRQ = JZRQ;

    }


    public String getYWFSRQ() {

        return this.YWFSRQ;

    }


    public void setYWFSRQ(String YWFSRQ) {

        this.YWFSRQ = YWFSRQ;

    }


    public String getZCZYQBJJE() {

        return this.ZCZYQBJJE;

    }


    public void setZCZYQBJJE(String ZCZYQBJJE) {

        this.ZCZYQBJJE = ZCZYQBJJE;

    }


    public String getFXJE() {

        return this.FXJE;

    }


    public void setFXJE(String FXJE) {

        this.FXJE = FXJE;

    }


}