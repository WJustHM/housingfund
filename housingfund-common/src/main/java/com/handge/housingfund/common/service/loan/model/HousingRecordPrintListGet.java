package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HousingRecordPrintListGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingRecordPrintListGet  implements Serializable {

    private String JKRZJHM;  //借款人证件号码

    private String YZM;  //验证码

    private ArrayList<HousingRecordPrintListGetInformation> information;  //还款记录列表

    private String JKRZJLX;  //借款人证件类型

    private String DKZH;  //贷款账号

    private String JKRXM;  //借款人姓名

    public String getJKRZJHM() {

        return this.JKRZJHM;

    }


    public void setJKRZJHM(String JKRZJHM) {

        this.JKRZJHM = JKRZJHM;

    }


    public String getYZM() {

        return this.YZM;

    }


    public void setYZM(String YZM) {

        this.YZM = YZM;

    }


    public ArrayList<HousingRecordPrintListGetInformation> getinformation() {

        return this.information;

    }


    public void setinformation(ArrayList<HousingRecordPrintListGetInformation> information) {

        this.information = information;

    }


    public String getJKRZJLX() {

        return this.JKRZJLX;

    }


    public void setJKRZJLX(String JKRZJLX) {

        this.JKRZJLX = JKRZJLX;

    }


    public String getDKZH() {

        return this.DKZH;

    }


    public void setDKZH(String DKZH) {

        this.DKZH = DKZH;

    }


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public String toString() {

        return "HousingRecordPrintListGet{" +

                "JKRZJHM='" + this.JKRZJHM + '\'' + "," +
                "YZM='" + this.YZM + '\'' + "," +
                "information='" + this.information + '\'' + "," +
                "JKRZJLX='" + this.JKRZJLX + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' +

                "}";

    }
}