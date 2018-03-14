package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/8/16.
 */
@XmlRootElement(name = "BorrowerInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class BorrowerInformation implements Serializable {

    private static final long serialVersionUID = 1401953351560585951L;
    private  String JCD;

    private String BLZL;

   private BorrowingInfo borrowingInfo;

   private ProvidentFundAccountInfo providentFundAccountInfo;

   private UnitInfos unitinfo;

   private OthersInfo otherinfo;

    public BorrowingInfo getBorrowingInfo() {
        return borrowingInfo;
    }

    public void setBorrowingInfo(BorrowingInfo borrowingInfo) {
        this.borrowingInfo = borrowingInfo;
    }

    public ProvidentFundAccountInfo getProvidentFundAccountInfo() {
        return providentFundAccountInfo;
    }

    public void setProvidentFundAccountInfo(ProvidentFundAccountInfo providentFundAccountInfo) {
        this.providentFundAccountInfo = providentFundAccountInfo;
    }

    public UnitInfos getUnitinfo() {
        return unitinfo;
    }

    public void setUnitinfo(UnitInfos unitinfo) {
        this.unitinfo = unitinfo;
    }

    public OthersInfo getOtherinfo() {
        return otherinfo;
    }

    public void setOtherinfo(OthersInfo otherinfo) {
        this.otherinfo = otherinfo;
    }

    public String getJCD() {
        return JCD;
    }

    public void setJCD(String JCD) {
        this.JCD = JCD;
    }



    public String getBLZL() {
        return BLZL;
    }

    public void setBLZL(String BLZL) {
        this.BLZL = BLZL;
    }

    @Override
    public String toString() {
        return "BorrowerInformation{" +
                "JCD='" + JCD + '\'' +
                ", BLZL='" + BLZL + '\'' +
                ", borrowingInfo=" + borrowingInfo +
                ", providentFundAccountInfo=" + providentFundAccountInfo +
                ", unitinfo=" + unitinfo +
                ", otherinfo=" + otherinfo +
                '}';
    }
}
