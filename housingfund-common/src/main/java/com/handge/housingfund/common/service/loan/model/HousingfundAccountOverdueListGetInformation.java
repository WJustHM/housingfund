package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HousingfundAccountOverdueListGetInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingfundAccountOverdueListGetInformation  implements Serializable {

    private String SSYQLXJE;  //实收逾期利息金额

    private String YQBJ;  //逾期本金

    private String YQLX;  //逾期利息

    private Boolean SFHK;  //是否还款

    private String SSRQ;  //实收日期

    private String SSYQFXJE;  //实收逾期罚息金额

    private String SSYQBJJE;  //实收逾期本金金额

    private String HKQC;  //还款期次

    private Integer YQQC;  //逾期期次

    private String YQFX;  //逾期罚息

    public String getSSYQLXJE() {

        return this.SSYQLXJE;

    }


    public void setSSYQLXJE(String SSYQLXJE) {

        this.SSYQLXJE = SSYQLXJE;

    }


    public String getYQBJ() {

        return this.YQBJ;

    }


    public void setYQBJ(String YQBJ) {

        this.YQBJ = YQBJ;

    }


    public String getYQLX() {

        return this.YQLX;

    }


    public void setYQLX(String YQLX) {

        this.YQLX = YQLX;

    }


    public Boolean getSFHK() {

        return this.SFHK;

    }


    public void setSFHK(Boolean SFHK) {

        this.SFHK = SFHK;

    }


    public String getSSRQ() {

        return this.SSRQ;

    }


    public void setSSRQ(String SSRQ) {

        this.SSRQ = SSRQ;

    }


    public String getSSYQFXJE() {

        return this.SSYQFXJE;

    }


    public void setSSYQFXJE(String SSYQFXJE) {

        this.SSYQFXJE = SSYQFXJE;

    }


    public String getSSYQBJJE() {

        return this.SSYQBJJE;

    }


    public void setSSYQBJJE(String SSYQBJJE) {

        this.SSYQBJJE = SSYQBJJE;

    }


    public String getHKQC() {

        return this.HKQC;

    }


    public void setHKQC(String HKQC) {

        this.HKQC = HKQC;

    }


    public Integer getYQQC() {

        return this.YQQC;

    }


    public void setYQQC(Integer YQQC) {

        this.YQQC = YQQC;

    }


    public String getYQFX() {

        return this.YQFX;

    }


    public void setYQFX(String YQFX) {

        this.YQFX = YQFX;

    }


    public String toString() {

        return "HousingfundAccountOverdueListGetInformation{" +

                "SSYQLXJE='" + this.SSYQLXJE + '\'' + "," +
                "YQBJ='" + this.YQBJ + '\'' + "," +
                "YQLX='" + this.YQLX + '\'' + "," +
                "SFHK='" + this.SFHK + '\'' + "," +
                "SSRQ='" + this.SSRQ + '\'' + "," +
                "SSYQFXJE='" + this.SSYQFXJE + '\'' + "," +
                "SSYQBJJE='" + this.SSYQBJJE + '\'' + "," +
                "HKQC='" + this.HKQC + '\'' + "," +
                "YQQC='" + this.YQQC + '\'' + "," +
                "YQFX='" + this.YQFX + '\'' +

                "}";

    }
}