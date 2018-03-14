package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/8/16.
 */
@XmlRootElement(name = "CommonBorrowerInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommonBorrowerInformation implements Serializable {

    private static final long serialVersionUID = -9163850555523049918L;
    private CommonBorrowerInfo commonBorrowerInformation;
    private CommonUnitInfo commonUnitInfo;
    private CommonProvidentFund commonProvidentFund;
    private String BLZL;
    private String CDGX;
    private String GTJKRGJJZH;
    private String JCD;



    public CommonBorrowerInfo getCommonBorrowerInformation() {
        return commonBorrowerInformation;
    }

    public void setCommonBorrowerInformation(CommonBorrowerInfo commonBorrowerInformation) {
        this.commonBorrowerInformation = commonBorrowerInformation;
    }

    public CommonUnitInfo getCommonUnitInfo() {
        return commonUnitInfo;
    }

    public void setCommonUnitInfo(CommonUnitInfo commonUnitInfo) {
        this.commonUnitInfo = commonUnitInfo;
    }

    public CommonProvidentFund getCommonProvidentFund() {
        return commonProvidentFund;
    }

    public void setCommonProvidentFund(CommonProvidentFund commonProvidentFund) {
        this.commonProvidentFund = commonProvidentFund;
    }

    public String getBLZL() {
        return BLZL;
    }

    public void setBLZL(String BLZL) {
        this.BLZL = BLZL;
    }

    public String getCDGX() {
        return CDGX;
    }

    public void setCDGX(String CDGX) {
        this.CDGX = CDGX;
    }

    public String getGTJKRGJJZH() {
        return GTJKRGJJZH;
    }

    public void setGTJKRGJJZH(String GTJKRGJJZH) {
        this.GTJKRGJJZH = GTJKRGJJZH;
    }

    public String getJCD() {
        return JCD;
    }

    public void setJCD(String JCD) {
        this.JCD = JCD;
    }

    @Override
    public String toString() {
        return "CommonBorrowerInformation{" +
                "commonBorrowerInformation=" + commonBorrowerInformation +
                ", commonUnitInfo=" + commonUnitInfo +
                ", commonProvidentFund=" + commonProvidentFund +
                ", BLZL='" + BLZL + '\'' +
                ", CDGX='" + CDGX + '\'' +
                ", GTJKRGJJZH='" + GTJKRGJJZH + '\'' +
                ", JCD='" + JCD + '\'' +
                '}';
    }
}
