package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HousingfundAccountPlanGetInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingfundAccountPlanGetInformation  implements Serializable {

    private String HKBJJE;  //还款本金金额

    private String FSE;  //发生额

    private String DKYE;  //贷款余额

    private String HKLXJE;  //还款利息金额

    private String HKRQ;  //还款日期

    private String HKQC; //还款期次

    public String getHKQC() {
        return HKQC;
    }

    public void setHKQC(String HKQC) {
        this.HKQC = HKQC;
    }

    public String getHKBJJE() {

        return this.HKBJJE;

    }


    public void setHKBJJE(String HKBJJE) {

        this.HKBJJE = HKBJJE;

    }


    public String getFSE() {

        return this.FSE;

    }


    public void setFSE(String FSE) {

        this.FSE = FSE;

    }


    public String getDKYE() {

        return this.DKYE;

    }


    public void setDKYE(String DKYE) {

        this.DKYE = DKYE;

    }


    public String getHKLXJE() {

        return this.HKLXJE;

    }


    public void setHKLXJE(String HKLXJE) {

        this.HKLXJE = HKLXJE;

    }


    public String getHKRQ() {

        return this.HKRQ;

    }


    public void setHKRQ(String HKRQ) {

        this.HKRQ = HKRQ;

    }

    @Override
    public String toString() {
        return "HousingfundAccountPlanGetInformation{" +
                "HKBJJE='" + HKBJJE + '\'' +
                ", FSE='" + FSE + '\'' +
                ", DKYE='" + DKYE + '\'' +
                ", HKLXJE='" + HKLXJE + '\'' +
                ", HKRQ='" + HKRQ + '\'' +
                ", HKQC='" + HKQC + '\'' +
                '}';
    }
}