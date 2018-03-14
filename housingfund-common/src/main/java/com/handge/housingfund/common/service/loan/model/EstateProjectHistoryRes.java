package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "EstateProjectHistoryRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstateProjectHistoryRes  implements Serializable {

    private String SLSJ;  //受理时间

    private String CZY;  //操作员

    private String CZNR;  //操作内容

    private String YWWD;  //业务网点

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


    public String getCZNR() {

        return this.CZNR;

    }


    public void setCZNR(String CZNR) {

        this.CZNR = CZNR;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String toString() {

        return "EstateProjectHistoryRes{" +

                "SLSJ='" + this.SLSJ + '\'' + "," +
                "CZY='" + this.CZY + '\'' + "," +
                "CZNR='" + this.CZNR + '\'' + "," +
                "YWWD='" + this.YWWD + '\'' +

                "}";

    }
}