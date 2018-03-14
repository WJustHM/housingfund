package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetApplicantResponseHouseInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetApplicantResponseHouseInformation  implements Serializable {

    private GetApplicantResponseHouseInformationPurchaseSecondInformation PurchaseSecondInformation;  // 二手房屋购买信息

    private String DKYT;  //贷款用途

    private String SFWESF;  //是否为二手房

    private GetApplicantResponseHouseInformationPurchaseFirstInformation PurchaseFirstInformation;  // 房屋购买信息

    private GetApplicantResponseHouseInformationOverhaulInformation OverhaulInformation;  // 大修信息

    private GetApplicantResponseHouseInformationBuildInformation BuildInformation;  // 自建，翻修信息

    public GetApplicantResponseHouseInformationPurchaseSecondInformation getPurchaseSecondInformation() {

        return this.PurchaseSecondInformation;

    }


    public void setPurchaseSecondInformation(GetApplicantResponseHouseInformationPurchaseSecondInformation PurchaseSecondInformation) {

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


    public GetApplicantResponseHouseInformationPurchaseFirstInformation getPurchaseFirstInformation() {

        return this.PurchaseFirstInformation;

    }


    public void setPurchaseFirstInformation(GetApplicantResponseHouseInformationPurchaseFirstInformation PurchaseFirstInformation) {

        this.PurchaseFirstInformation = PurchaseFirstInformation;

    }


    public GetApplicantResponseHouseInformationOverhaulInformation getOverhaulInformation() {

        return this.OverhaulInformation;

    }


    public void setOverhaulInformation(GetApplicantResponseHouseInformationOverhaulInformation OverhaulInformation) {

        this.OverhaulInformation = OverhaulInformation;

    }


    public GetApplicantResponseHouseInformationBuildInformation getBuildInformation() {

        return this.BuildInformation;

    }


    public void setBuildInformation(GetApplicantResponseHouseInformationBuildInformation BuildInformation) {

        this.BuildInformation = BuildInformation;

    }

    @Override
    public String toString() {
        return "GetApplicantResponseHouseInformation{" +
                "PurchaseSecondInformation=" + PurchaseSecondInformation +
                ", DKYT='" + DKYT + '\'' +
                ", SFWESF='" + SFWESF + '\'' +
                ", PurchaseFirstInformation=" + PurchaseFirstInformation +
                ", OverhaulInformation=" + OverhaulInformation +
                ", BuildInformation=" + BuildInformation +
                '}';
    }
}