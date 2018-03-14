package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "LoanMountCalculationPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanMountCalculationPost  implements Serializable {

    private String JKRJCYE1;  //借款人1缴存余额

    private String SJXS;  //时间系数

    private String ZDRXB;  //主贷人性别

    private String JKRJCYE2;  //借款人2缴存余额

    private String ZDRNL;  //主贷人年龄

    public String getJKRJCYE1() {

        return this.JKRJCYE1;

    }


    public void setJKRJCYE1(String JKRJCYE1) {

        this.JKRJCYE1 = JKRJCYE1;

    }


    public String getSJXS() {

        return this.SJXS;

    }


    public void setSJXS(String SJXS) {

        this.SJXS = SJXS;

    }


    public String getZDRXB() {

        return this.ZDRXB;

    }


    public void setZDRXB(String ZDRXB) {

        this.ZDRXB = ZDRXB;

    }


    public String getJKRJCYE2() {

        return this.JKRJCYE2;

    }


    public void setJKRJCYE2(String JKRJCYE2) {

        this.JKRJCYE2 = JKRJCYE2;

    }


    public String getZDRNL() {

        return this.ZDRNL;

    }


    public void setZDRNL(String ZDRNL) {

        this.ZDRNL = ZDRNL;

    }


    public String toString() {

        return "LoanMountCalculationPost{" +

                "JKRJCYE1='" + this.JKRJCYE1 + '\'' + "," +
                "SJXS='" + this.SJXS + '\'' + "," +
                "ZDRXB='" + this.ZDRXB + '\'' + "," +
                "JKRJCYE2='" + this.JKRJCYE2 + '\'' + "," +
                "ZDRNL='" + this.ZDRNL + '\'' +

                "}";

    }
}