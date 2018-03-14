package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "LoanHistory")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanHistory  implements Serializable {

    private String YWWD;  //业务网点

    private String SLSJ;  //受理时间

    private String CZY;  //操作员

    private String CZQD;  //操作渠道

    private String ZCNR;  //操作内容

    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getSLSJ() {

        return this.SLSJ;

    }


    public void setSLSJ(String SLSJ) {

        this.SLSJ = SLSJ;

    }


    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }


    public String getCZQD() {

        return this.CZQD;

    }


    public void setCZQD(String CZQD) {

        this.CZQD = CZQD;

    }


    public String getZCNR() {

        return this.ZCNR;

    }


    public void setZCNR(String ZCNR) {

        this.ZCNR = ZCNR;

    }


    public String toString() {

        return "LoanHistory{" +

                "YWWD='" + this.YWWD + '\'' + "," +
                "SLSJ='" + this.SLSJ + '\'' + "," +
                "CZY='" + this.CZY + '\'' + "," +
                "CZQD='" + this.CZQD + '\'' + "," +
                "ZCNR='" + this.ZCNR + '\'' +

                "}";

    }
}