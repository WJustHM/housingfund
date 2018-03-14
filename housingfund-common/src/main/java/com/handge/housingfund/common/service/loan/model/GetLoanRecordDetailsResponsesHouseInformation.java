package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetLoanRecordDetailsResponsesHouseInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoanRecordDetailsResponsesHouseInformation  implements Serializable {

    private GetLoanRecordDetailsResponsesHouseInformationPurchaseSecondInformation PurchaseSecondInformation;  //购买二手房

    private String DKYT;  //贷款用途（0：所有 1：购买 2：自建、翻修 3：大修）

    private String SFWESF;  //是否为二手房

    private GetLoanRecordDetailsResponsesHouseInformationPurchaseFirstInformation PurchaseFirstInformation;  //购买非二手房

    private GetLoanRecordDetailsResponsesHouseInformationOverhaulInformation OverhaulInformation;  //大修

    private GetLoanRecordDetailsResponsesHouseInformationBuildInformation BuildInformation;  //自建、翻修

    private String BLZL;    //办理资料

    public String getBLZL() {
        return BLZL;
    }

    public void setBLZL(String BLZL) {
        this.BLZL = BLZL;
    }

    public GetLoanRecordDetailsResponsesHouseInformationPurchaseSecondInformation getPurchaseSecondInformation() {

        return this.PurchaseSecondInformation;

    }


    public void setPurchaseSecondInformation(GetLoanRecordDetailsResponsesHouseInformationPurchaseSecondInformation PurchaseSecondInformation) {

        this.PurchaseSecondInformation = PurchaseSecondInformation;

    }


    public String getDKYT() {

        return this.DKYT;

    }


    public void setDKYT(String DKYT) {

        this.DKYT = DKYT;

    }


    public String getSFWESF() {

        return this.SFWESF;

    }


    public void setSFWESF(String SFWESF) {

        this.SFWESF = SFWESF;

    }


    public GetLoanRecordDetailsResponsesHouseInformationPurchaseFirstInformation getPurchaseFirstInformation() {

        return this.PurchaseFirstInformation;

    }


    public void setPurchaseFirstInformation(GetLoanRecordDetailsResponsesHouseInformationPurchaseFirstInformation PurchaseFirstInformation) {

        this.PurchaseFirstInformation = PurchaseFirstInformation;

    }


    public GetLoanRecordDetailsResponsesHouseInformationOverhaulInformation getOverhaulInformation() {

        return this.OverhaulInformation;

    }


    public void setOverhaulInformation(GetLoanRecordDetailsResponsesHouseInformationOverhaulInformation OverhaulInformation) {

        this.OverhaulInformation = OverhaulInformation;

    }


    public GetLoanRecordDetailsResponsesHouseInformationBuildInformation getBuildInformation() {

        return this.BuildInformation;

    }


    public void setBuildInformation(GetLoanRecordDetailsResponsesHouseInformationBuildInformation BuildInformation) {

        this.BuildInformation = BuildInformation;

    }


    @Override
    public String toString() {
        return "GetLoanRecordDetailsResponsesHouseInformation{" +
                "PurchaseSecondInformation=" + PurchaseSecondInformation +
                ", DKYT='" + DKYT + '\'' +
                ", SFWESF='" + SFWESF + '\'' +
                ", PurchaseFirstInformation=" + PurchaseFirstInformation +
                ", OverhaulInformation=" + OverhaulInformation +
                ", BuildInformation=" + BuildInformation +
                ", BLZL='" + BLZL + '\'' +
                '}';
    }
}