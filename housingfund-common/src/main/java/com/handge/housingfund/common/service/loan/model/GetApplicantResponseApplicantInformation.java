package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetApplicantResponseApplicantInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetApplicantResponseApplicantInformation  implements Serializable {

    private GetApplicantResponseApplicantInformationBorrowerInformation BorrowerInformation;  // 借款人信息

    private  String    BLZL;   //办理资料  //上传资料

    private String JKRGJJZH;  //借款人公积金账号

    private GetApplicantResponseApplicantInformationUnitInformation UnitInformation;  // 单位信息

    private GetApplicantResponseApplicantInformationAccountInformation AccountInformation;  // 个人公积金信息

    private String JCD;  //缴存地

    public GetApplicantResponseApplicantInformationBorrowerInformation getBorrowerInformation() {

        return this.BorrowerInformation;

    }


    public void setBorrowerInformation(GetApplicantResponseApplicantInformationBorrowerInformation BorrowerInformation) {

        this.BorrowerInformation = BorrowerInformation;

    }


    public String getSCZL() {

        return this.BLZL;

    }


    public void setSCZL(String SCZL) {

        this.BLZL = SCZL;

    }


    public String getJKRGJJZH() {

        return this.JKRGJJZH;

    }


    public void setJKRGJJZH(String JKRGJJZH) {

        this.JKRGJJZH = JKRGJJZH;

    }


    public GetApplicantResponseApplicantInformationUnitInformation getUnitInformation() {

        return this.UnitInformation;

    }


    public void setUnitInformation(GetApplicantResponseApplicantInformationUnitInformation UnitInformation) {

        this.UnitInformation = UnitInformation;

    }


    public GetApplicantResponseApplicantInformationAccountInformation getAccountInformation() {

        return this.AccountInformation;

    }


    public void setAccountInformation(GetApplicantResponseApplicantInformationAccountInformation AccountInformation) {

        this.AccountInformation = AccountInformation;

    }


    public String getJCD() {

        return this.JCD;

    }


    public void setJCD(String JCD) {

        this.JCD = JCD;

    }


    public String toString() {

        return "GetApplicantResponseApplicantInformation{" +

                "BorrowerInformation='" + this.BorrowerInformation + '\'' + "," +
                "BLZL='" + this.BLZL + '\'' + "," +
                "JKRGJJZH='" + this.JKRGJJZH + '\'' + "," +
                "UnitInformation='" + this.UnitInformation + '\'' + "," +
                "AccountInformation='" + this.AccountInformation + '\'' + "," +
                "JCD='" + this.JCD + '\'' +

                "}";

    }
}