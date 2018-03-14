package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetLoanRecordDetailsResponses")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoanRecordDetailsResponses implements Serializable {

    private GetLoanRecordDetailsResponsesApplicantInformation ApplicantInformation;  //申请人信息

    private GetLoanRecordDetailsResponsesCommonBorrowerInformation CommonBorrowerInformation;  //共同借款人信息

    private GetLoanRecordDetailsResponsesHouseInformation HouseInformation;  //房屋信息

    private GetLoanRecordDetailsResponsesCapitalInformation CapitalInformation;  //资金信息

    private GetLoanRecordDetailsResponsesCollateralInformation CollateralInformation;  //担保信息

    private String BLZL;  //其他办理资料

    private String HTXX;  //合同信息(合同UUID)

    private GetLoanRecordDetailsResponsesManagerInformation managerInformation;  //办理信息

    private GetLoanRecordDetailsResponsesLoanAccountInformation LoanAccountInformation;  //贷款账户

    public GetLoanRecordDetailsResponsesManagerInformation getmanagerInformation() {

        return this.managerInformation;

    }


    public void setmanagerInformation(GetLoanRecordDetailsResponsesManagerInformation managerInformation) {

        this.managerInformation = managerInformation;

    }


    public String getHTXX() {
        return HTXX;
    }

    public void setHTXX(String HTXX) {
        this.HTXX = HTXX;
    }

    public GetLoanRecordDetailsResponsesHouseInformation getHouseInformation() {

        return this.HouseInformation;

    }


    public void setHouseInformation(GetLoanRecordDetailsResponsesHouseInformation HouseInformation) {

        this.HouseInformation = HouseInformation;

    }


    public GetLoanRecordDetailsResponsesLoanAccountInformation getLoanAccountInformation() {

        return this.LoanAccountInformation;

    }


    public void setLoanAccountInformation(GetLoanRecordDetailsResponsesLoanAccountInformation LoanAccountInformation) {

        this.LoanAccountInformation = LoanAccountInformation;

    }


    public GetLoanRecordDetailsResponsesCommonBorrowerInformation getCommonBorrowerInformation() {

        return this.CommonBorrowerInformation;

    }


    public void setCommonBorrowerInformation(GetLoanRecordDetailsResponsesCommonBorrowerInformation CommonBorrowerInformation) {

        this.CommonBorrowerInformation = CommonBorrowerInformation;

    }


    public String getQTZL() {

        return this.BLZL;

    }


    public void setQTZL(String QTZL) {

        this.BLZL = QTZL;

    }


    public GetLoanRecordDetailsResponsesCapitalInformation getCapitalInformation() {

        return this.CapitalInformation;

    }


    public void setCapitalInformation(GetLoanRecordDetailsResponsesCapitalInformation CapitalInformation) {

        this.CapitalInformation = CapitalInformation;

    }


    public GetLoanRecordDetailsResponsesApplicantInformation getApplicantInformation() {

        return this.ApplicantInformation;

    }


    public void setApplicantInformation(GetLoanRecordDetailsResponsesApplicantInformation ApplicantInformation) {

        this.ApplicantInformation = ApplicantInformation;

    }


    public GetLoanRecordDetailsResponsesCollateralInformation getCollateralInformation() {

        return this.CollateralInformation;

    }


    public void setCollateralInformation(GetLoanRecordDetailsResponsesCollateralInformation CollateralInformation) {

        this.CollateralInformation = CollateralInformation;

    }

    @Override
    public String toString() {
        return "GetLoanRecordDetailsResponses{" +
                "ApplicantInformation=" + ApplicantInformation +
                ", CommonBorrowerInformation=" + CommonBorrowerInformation +
                ", HouseInformation=" + HouseInformation +
                ", CapitalInformation=" + CapitalInformation +
                ", CollateralInformation=" + CollateralInformation +
                ", BLZL='" + BLZL + '\'' +
                ", HTXX='" + HTXX + '\'' +
                ", managerInformation=" + managerInformation +
                ", LoanAccountInformation=" + LoanAccountInformation +
                '}';
    }
}