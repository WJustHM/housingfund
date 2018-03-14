package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "GetApplicantResponseCollateralInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetApplicantResponseCollateralInformation  implements Serializable {

    private ArrayList<GetApplicantResponseCollateralInformationMortgageInformation> MortgageInformation;  //抵押物信息

    private  String    BLZL;   //办理资料  //上传资料

    private String DKDBLX;  //贷款担保类型

    private ArrayList<GetApplicantResponseCollateralInformationPledgeInformation> PledgeInformation;  //质押物信息

    private ArrayList<GetApplicantResponseCollateralInformationGuaranteeInformation> GuaranteeInformation;  //担保信息

    public ArrayList<GetApplicantResponseCollateralInformationMortgageInformation> getMortgageInformation() {

        return this.MortgageInformation;

    }


    public void setMortgageInformation(ArrayList<GetApplicantResponseCollateralInformationMortgageInformation> MortgageInformation) {

        this.MortgageInformation = MortgageInformation;

    }


    public String getSCZL() {

        return this.BLZL;

    }


    public void setSCZL(String SCZL) {

        this.BLZL = SCZL;

    }


    public String getDKDBLX() {

        return this.DKDBLX;

    }


    public void setDKDBLX(String DKDBLX) {

        this.DKDBLX = DKDBLX;

    }


    public ArrayList<GetApplicantResponseCollateralInformationPledgeInformation> getPledgeInformation() {

        return this.PledgeInformation;

    }


    public void setPledgeInformation(ArrayList<GetApplicantResponseCollateralInformationPledgeInformation> PledgeInformation) {

        this.PledgeInformation = PledgeInformation;

    }


    public ArrayList<GetApplicantResponseCollateralInformationGuaranteeInformation> getGuaranteeInformation() {

        return this.GuaranteeInformation;

    }


    public void setGuaranteeInformation(ArrayList<GetApplicantResponseCollateralInformationGuaranteeInformation> GuaranteeInformation) {

        this.GuaranteeInformation = GuaranteeInformation;

    }


    public String toString() {

        return "GetApplicantResponseCollateralInformation{" +

                "MortgageInformation='" + this.MortgageInformation + '\'' + "," +
                "BLZL='" + this.BLZL + '\'' + "," +
                "DKDBLX='" + this.DKDBLX + '\'' + "," +
                "PledgeInformation='" + this.PledgeInformation + '\'' + "," +
                "GuaranteeInformation='" + this.GuaranteeInformation + '\'' +

                "}";

    }
}