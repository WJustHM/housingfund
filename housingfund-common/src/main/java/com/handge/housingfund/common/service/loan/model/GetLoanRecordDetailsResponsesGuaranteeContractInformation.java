package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetLoanRecordDetailsResponsesGuaranteeContractInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoanRecordDetailsResponsesGuaranteeContractInformation  implements Serializable {

    private String JKHTBH;  //借款合同编号

    private GetLoanRecordDetailsResponsesGuaranteeContractInformationPledgeInformation PledgeInformation;  //

    private GetLoanRecordDetailsResponsesGuaranteeContractInformationGuaranteeInformation GuaranteeInformation;  //保证

    private GetLoanRecordDetailsResponsesGuaranteeContractInformationMortgageInformation MortgageInformation;  //

    private String DBJGMC;  //担保机构名称

    private String DBHTBH;  //担保合同编号

    public String getJKHTBH() {

        return this.JKHTBH;

    }


    public void setJKHTBH(String JKHTBH) {

        this.JKHTBH = JKHTBH;

    }


    public GetLoanRecordDetailsResponsesGuaranteeContractInformationPledgeInformation getPledgeInformation() {

        return this.PledgeInformation;

    }


    public void setPledgeInformation(GetLoanRecordDetailsResponsesGuaranteeContractInformationPledgeInformation PledgeInformation) {

        this.PledgeInformation = PledgeInformation;

    }


    public GetLoanRecordDetailsResponsesGuaranteeContractInformationGuaranteeInformation getGuaranteeInformation() {

        return this.GuaranteeInformation;

    }


    public void setGuaranteeInformation(GetLoanRecordDetailsResponsesGuaranteeContractInformationGuaranteeInformation GuaranteeInformation) {

        this.GuaranteeInformation = GuaranteeInformation;

    }


    public GetLoanRecordDetailsResponsesGuaranteeContractInformationMortgageInformation getMortgageInformation() {

        return this.MortgageInformation;

    }


    public void setMortgageInformation(GetLoanRecordDetailsResponsesGuaranteeContractInformationMortgageInformation MortgageInformation) {

        this.MortgageInformation = MortgageInformation;

    }


    public String getDBJGMC() {

        return this.DBJGMC;

    }


    public void setDBJGMC(String DBJGMC) {

        this.DBJGMC = DBJGMC;

    }


    public String getDBHTBH() {

        return this.DBHTBH;

    }


    public void setDBHTBH(String DBHTBH) {

        this.DBHTBH = DBHTBH;

    }


    public String toString() {

        return "GetLoanRecordDetailsResponsesGuaranteeContractInformation{" +

                "JKHTBH='" + this.JKHTBH + '\'' + "," +
                "PledgeInformation='" + this.PledgeInformation + '\'' + "," +
                "GuaranteeInformation='" + this.GuaranteeInformation + '\'' + "," +
                "MortgageInformation='" + this.MortgageInformation + '\'' + "," +
                "DBJGMC='" + this.DBJGMC + '\'' + "," +
                "DBHTBH='" + this.DBHTBH + '\'' +

                "}";

    }
}