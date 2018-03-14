package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ReviewFailDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReviewFailDetails  implements Serializable {

    private String SHJG;  //审核结果（1：通过，2：不通过）

    private String YWWD;  //业务网点

    private String CZY;  //操作员

    private String YYMS;  //原因描述

    private String YYLX;  //原因类型（0：资料不完善 1：信息不正确）

    public String getSHJG() {

        return this.SHJG;

    }


    public void setSHJG(String SHJG) {

        this.SHJG = SHJG;

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


    public String getYYMS() {

        return this.YYMS;

    }


    public void setYYMS(String YYMS) {

        this.YYMS = YYMS;

    }


    public String getYYLX() {

        return this.YYLX;

    }


    public void setYYLX(String YYLX) {

        this.YYLX = YYLX;

    }


    public String toString() {

        return "ReviewFailDetails{" +

                "SHJG='" + this.SHJG + '\'' + "," +
                "YWWD='" + this.YWWD + '\'' + "," +
                "CZY='" + this.CZY + '\'' + "," +
                "YYMS='" + this.YYMS + '\'' + "," +
                "YYLX='" + this.YYLX + '\'' +

                "}";

    }
}