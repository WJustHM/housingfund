package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HousingfundAccountBusinessListGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingfundAccountBusinessListGet  implements Serializable {

    private String JKRZJHM;  //借款人证件号码

    private ArrayList<HousingfundAccountBusinessListGetInformation> information;  //业务信息集合

    private String JKRZJLX;  //借款人证件类型

    private String DKZH;  //贷款账号

    private String JKRXM;  //借款人姓名

    public String getJKRZJHM() {

        return this.JKRZJHM;

    }


    public void setJKRZJHM(String JKRZJHM) {

        this.JKRZJHM = JKRZJHM;

    }


    public ArrayList<HousingfundAccountBusinessListGetInformation> getinformation() {

        return this.information;

    }


    public void setinformation(ArrayList<HousingfundAccountBusinessListGetInformation> information) {

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

        return "HousingfundAccountBusinessListGet{" +

                "JKRZJHM='" + this.JKRZJHM + '\'' + "," +
                "information='" + this.information + '\'' + "," +
                "JKRZJLX='" + this.JKRZJLX + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' +

                "}";

    }
}