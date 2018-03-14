package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HousingRecordPrintListGetInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingRecordPrintListGetInformation  implements Serializable {

    private String YWLSH;  //业务流水号

    private String FSE;  //发生额

    private String DKYWMXLX;  //贷款业务明细类型

    private String BJJE;  //本金金额

    private String JZRQ;  //记账日期

    private String YWFSRQ;  //业务发生日期

    private Integer DQQC;  //当期期次

    private String LXJE;  //利息金额

    private String FXJE;  //罚息金额

    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getFSE() {

        return this.FSE;

    }


    public void setFSE(String FSE) {

        this.FSE = FSE;

    }


    public String getDKYWMXLX() {

        return this.DKYWMXLX;

    }


    public void setDKYWMXLX(String DKYWMXLX) {

        this.DKYWMXLX = DKYWMXLX;

    }


    public String getBJJE() {

        return this.BJJE;

    }


    public void setBJJE(String BJJE) {

        this.BJJE = BJJE;

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


    public Integer getDQQC() {

        return this.DQQC;

    }


    public void setDQQC(Integer DQQC) {

        this.DQQC = DQQC;

    }


    public String getLXJE() {

        return this.LXJE;

    }


    public void setLXJE(String LXJE) {

        this.LXJE = LXJE;

    }


    public String getFXJE() {

        return this.FXJE;

    }


    public void setFXJE(String FXJE) {

        this.FXJE = FXJE;

    }


    public String toString() {

        return "HousingRecordPrintListGetInformation{" +

                "YWLSH='" + this.YWLSH + '\'' + "," +
                "FSE='" + this.FSE + '\'' + "," +
                "DKYWMXLX='" + this.DKYWMXLX + '\'' + "," +
                "BJJE='" + this.BJJE + '\'' + "," +
                "JZRQ='" + this.JZRQ + '\'' + "," +
                "YWFSRQ='" + this.YWFSRQ + '\'' + "," +
                "DQQC='" + this.DQQC + '\'' + "," +
                "LXJE='" + this.LXJE + '\'' + "," +
                "FXJE='" + this.FXJE + '\'' +

                "}";

    }
}