package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@XmlRootElement(name = "AccountSquarepdfInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountSquarepdfInformation implements Serializable {

    private String XingMing; //姓名

    private Date JQR; //结清日期

    private String ZJHM; //证件号码

    private String DKZH; //贷款账号

    private String JKJE_DX; //借款金额(大写)

    private BigDecimal JKJE; //借款金额

    private BigDecimal JKQS; //借款期数

    private Date FKR; //放款日

    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

    public Date getJQR() {
        return JQR;
    }

    public void setJQR(Date JQR) {
        this.JQR = JQR;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getDKZH() {
        return DKZH;
    }

    public void setDKZH(String DKZH) {
        this.DKZH = DKZH;
    }

    public String getJKJE_DX() {
        return JKJE_DX;
    }

    public void setJKJE_DX(String JKJE_DX) {
        this.JKJE_DX = JKJE_DX;
    }

    public BigDecimal getJKJE() {
        return JKJE;
    }

    public void setJKJE(BigDecimal JKJE) {
        this.JKJE = JKJE;
    }

    public BigDecimal getJKQS() {
        return JKQS;
    }

    public void setJKQS(BigDecimal JKQS) {
        this.JKQS = JKQS;
    }

    public Date getFKR() {
        return FKR;
    }

    public void setFKR(Date FKR) {
        this.FKR = FKR;
    }

    public String toString(){

        return "AccountSquarepdfInformation{" +

                "XingMing='" + this.XingMing + '\'' + "," +
                "JQR='" + this.JQR + '\'' + "," +
                "ZJHM='" + this.ZJHM + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' + "," +
                "JKJE_DX='" + this.JKJE_DX + '\'' + "," +
                "JKJE='" + this.JKJE + '\'' + "," +
                "JKQS='" + this.JKQS + '\'' +
                "FKR='" + this.FKR + '\'' +
                "}";

    }
}
