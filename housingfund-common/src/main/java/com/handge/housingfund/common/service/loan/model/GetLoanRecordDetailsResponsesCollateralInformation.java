package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "GetLoanRecordDetailsResponsesCollateralInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoanRecordDetailsResponsesCollateralInformation implements Serializable {

    private String DKDBLX;  //贷款担保类型 01 抵押 02质押 03保证

    private ArrayList<GetLoanRecordDetailsResponsesCollateralInformationMortgageInformation> MortgageInformations;  //抵押

    private ArrayList<GetLoanRecordDetailsResponsesCollateralInformationPledgeInformation> PledgeInformations;  //质押

    private ArrayList<GetLoanRecordDetailsResponsesCollateralInformationGuaranteeInformation> GuaranteeInformations;  //保证

    private String BLZL;  //提交资料

    public ArrayList<GetLoanRecordDetailsResponsesCollateralInformationMortgageInformation> getMortgageInformations() {
        return MortgageInformations;
    }

    public void setMortgageInformations(ArrayList<GetLoanRecordDetailsResponsesCollateralInformationMortgageInformation> mortgageInformations) {
        MortgageInformations = mortgageInformations;
    }

    public ArrayList<GetLoanRecordDetailsResponsesCollateralInformationPledgeInformation> getPledgeInformations() {
        return PledgeInformations;
    }

    public void setPledgeInformations(ArrayList<GetLoanRecordDetailsResponsesCollateralInformationPledgeInformation> pledgeInformations) {
        PledgeInformations = pledgeInformations;
    }

    public ArrayList<GetLoanRecordDetailsResponsesCollateralInformationGuaranteeInformation> getGuaranteeInformations() {
        return GuaranteeInformations;
    }

    public void setGuaranteeInformations(ArrayList<GetLoanRecordDetailsResponsesCollateralInformationGuaranteeInformation> guaranteeInformations) {
        GuaranteeInformations = guaranteeInformations;
    }

    public String getDKDBLX() {

        return this.DKDBLX;

    }


    public void setDKDBLX(String DKDBLX) {

        this.DKDBLX = DKDBLX;

    }


    public String getBLZL() {

        return this.BLZL;

    }


    public void setBLZL(String BLZL) {

        this.BLZL = BLZL;

    }


    @Override
    public String toString() {
        return "GetLoanRecordDetailsResponsesCollateralInformation{" +
                "DKDBLX='" + DKDBLX + '\'' +
                ", MortgageInformations=" + MortgageInformations +
                ", PledgeInformations=" + PledgeInformations +
                ", GuaranteeInformations=" + GuaranteeInformations +
                ", BLZL='" + BLZL + '\'' +
                '}';
    }
}