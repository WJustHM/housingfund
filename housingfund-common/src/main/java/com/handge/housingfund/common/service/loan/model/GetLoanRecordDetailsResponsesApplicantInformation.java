package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetLoanRecordDetailsResponsesApplicantInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoanRecordDetailsResponsesApplicantInformation  implements Serializable {

    private String DKZH;  //业务流水号

    private String BLZL;  //提交资料

    private String JCD;  //缴存地

    private String JKRGJJZH;  //借款人公积金账号

    private String GTJKR;  //共同借款人（0有，1无）

    private GetLoanRecordDetailsResponsesApplicantInformationBorrowerInformation BorrowerInformation;  //借款人信息

    private GetLoanRecordDetailsResponsesApplicantInformationUnitInformation UnitInformation;  //单位信息

    private GetLoanRecordDetailsResponsesApplicantInformationAccountInformation AccountInformation;  //公积金账户信息

    public String getDKZH() {
        return DKZH;
    }

    public void setDKZH(String DKZH) {
        this.DKZH = DKZH;
    }

    public String getBLZL() {

        return this.BLZL;

    }


    public void setBLZL(String BLZL) {

        this.BLZL = BLZL;

    }


    public GetLoanRecordDetailsResponsesApplicantInformationBorrowerInformation getBorrowerInformation() {

        return this.BorrowerInformation;

    }


    public void setBorrowerInformation(GetLoanRecordDetailsResponsesApplicantInformationBorrowerInformation BorrowerInformation) {

        this.BorrowerInformation = BorrowerInformation;

    }


    public String getJKRGJJZH() {

        return this.JKRGJJZH;

    }


    public void setJKRGJJZH(String JKRGJJZH) {

        this.JKRGJJZH = JKRGJJZH;

    }


    public GetLoanRecordDetailsResponsesApplicantInformationUnitInformation getUnitInformation() {

        return this.UnitInformation;

    }


    public void setUnitInformation(GetLoanRecordDetailsResponsesApplicantInformationUnitInformation UnitInformation) {

        this.UnitInformation = UnitInformation;

    }


    public String getJCD() {

        return this.JCD;

    }


    public void setJCD(String JCD) {

        this.JCD = JCD;

    }


    public GetLoanRecordDetailsResponsesApplicantInformationAccountInformation getAccountInformation() {

        return this.AccountInformation;

    }


    public void setAccountInformation(GetLoanRecordDetailsResponsesApplicantInformationAccountInformation AccountInformation) {

        this.AccountInformation = AccountInformation;

    }


    public String getGTJKR() {

        return this.GTJKR;

    }


    public void setGTJKR(String GTJKR) {

        this.GTJKR = GTJKR;

    }


    @Override
    public String toString() {
        return "GetLoanRecordDetailsResponsesApplicantInformation{" +
                "DKZH='" + DKZH + '\'' +
                ", BLZL='" + BLZL + '\'' +
                ", BorrowerInformation=" + BorrowerInformation +
                ", JKRGJJZH='" + JKRGJJZH + '\'' +
                ", UnitInformation=" + UnitInformation +
                ", JCD='" + JCD + '\'' +
                ", AccountInformation=" + AccountInformation +
                ", GTJKR='" + GTJKR + '\'' +
                '}';
    }
}