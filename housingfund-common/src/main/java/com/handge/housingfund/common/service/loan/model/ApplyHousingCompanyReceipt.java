package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplyHousingCompanyReceipt")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplyHousingCompanyReceipt  implements Serializable {

    private String YZM;  //验证码

    private String YWLSH;  //业务流水号

    private String TZRQ;  //填制日期

    private ApplyHousingCompanyReceiptUnitInfo UnitInfo;  //单位信息

    private String CZY; //操作员


    private String SHR; //审核人

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getYZM() {

        return this.YZM;

    }
    public String getSHR() {
        return SHR;
    }

    public void setSHR(String SHR) {
        this.SHR = SHR;
    }

    public void setYZM(String YZM) {

        this.YZM = YZM;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getTZRQ() {

        return this.TZRQ;

    }


    public void setTZRQ(String TZRQ) {

        this.TZRQ = TZRQ;

    }


    public ApplyHousingCompanyReceiptUnitInfo getUnitInfo() {

        return this.UnitInfo;

    }


    public void setUnitInfo(ApplyHousingCompanyReceiptUnitInfo UnitInfo) {

        this.UnitInfo = UnitInfo;

    }


    @Override
    public String toString() {
        return "ApplyHousingCompanyReceipt{" +
                "YZM='" + YZM + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", TZRQ='" + TZRQ + '\'' +
                ", UnitInfo=" + UnitInfo +
                ", CZY='" + CZY + '\'' +
                ", SHR='" + SHR + '\'' +
                '}';
    }
}