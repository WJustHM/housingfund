package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HistoryRecordResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class HistoryRecordResponse  implements Serializable {

    private String SLSJ;  //受理时间

    private String JKRZJLX;  //借款人证件类型

    private String CZQD;  //操作渠道

    private String YWWD;  //业务网点

    private String CZY;  //操作员

    private String CZNR;  //操作内容

    private String JKRXM;  //借款人姓名


    public String getSLSJ() {

        return this.SLSJ;

    }


    public void setSLSJ(String SLSJ) {

        this.SLSJ = SLSJ;

    }


    public String getJKRZJLX() {

        return this.JKRZJLX;

    }


    public void setJKRZJLX(String JKRZJLX) {

        this.JKRZJLX = JKRZJLX;

    }


    public String getCZQD() {

        return this.CZQD;

    }


    public void setCZQD(String CZQD) {

        this.CZQD = CZQD;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

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


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public String toString() {

        return "HistoryRecordResponse{" +

                "SLSJ='" + this.SLSJ + '\'' + "," +
                "JKRZJLX='" + this.JKRZJLX + '\'' + "," +
                "CZQD='" + this.CZQD + '\'' + "," +
                "YWWD='" + this.YWWD + '\'' + "," +
                "CZY='" + this.CZY + '\'' + "," +
                "CZNR='" + this.CZNR + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' +

                "}";

    }
}