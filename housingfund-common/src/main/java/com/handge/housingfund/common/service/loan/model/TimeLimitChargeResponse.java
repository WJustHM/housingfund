package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "TimeLimitChargeResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class TimeLimitChargeResponse  implements Serializable {

    private String WD_id;  //网点id

    private  String    BLZL;   //办理资料  // 办理资料

    private String ZQHQS;  //展期后期数

    private String ZQYYSM;  //展期原因说明

    private String DKZH;  //贷款账户

    public String getWD_id() {

        return this.WD_id;

    }


    public void setWD_id(String WD_id) {

        this.WD_id = WD_id;

    }


    public String getSCZL() {

        return this.BLZL;

    }


    public void setSCZL(String SCZL) {

        this.BLZL = SCZL;

    }


    public String getZQHQS() {

        return this.ZQHQS;

    }


    public void setZQHQS(String ZQHQS) {

        this.ZQHQS = ZQHQS;

    }


    public String getZQYYSM() {

        return this.ZQYYSM;

    }


    public void setZQYYSM(String ZQYYSM) {

        this.ZQYYSM = ZQYYSM;

    }


    public String getDKZH() {

        return this.DKZH;

    }


    public void setDKZH(String DKZH) {

        this.DKZH = DKZH;

    }


    public String toString() {

        return "TimeLimitChargeResponse{" +

                "WD_id='" + this.WD_id + '\'' + "," +
                "BLZL='" + this.BLZL + '\'' + "," +
                "ZQHQS='" + this.ZQHQS + '\'' + "," +
                "ZQYYSM='" + this.ZQYYSM + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' +

                "}";

    }
}