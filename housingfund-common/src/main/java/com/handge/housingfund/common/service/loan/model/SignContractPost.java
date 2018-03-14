package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "SignContractPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignContractPost implements Serializable {

    private SignContractPostGuaranteeContractInformation GuaranteeContractInformation;  //

    private String CZY;  //操作员

    private SignContractPostContractInformation ContractInformation;  //

    private String YWWD;  //业务网点

    private String BLZL;

    public SignContractPostGuaranteeContractInformation getGuaranteeContractInformation() {

        return this.GuaranteeContractInformation;

    }


    public void setGuaranteeContractInformation(SignContractPostGuaranteeContractInformation GuaranteeContractInformation) {

        this.GuaranteeContractInformation = GuaranteeContractInformation;

    }


    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }


    public SignContractPostContractInformation getContractInformation() {

        return this.ContractInformation;

    }


    public void setContractInformation(SignContractPostContractInformation ContractInformation) {

        this.ContractInformation = ContractInformation;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }

    public String getBLZL() {
        return BLZL;
    }

    public void setBLZL(String BLZL) {
        this.BLZL = BLZL;
    }


    public String toString() {

        return "SignContractPost{" +

                "GuaranteeContractInformation='" + this.GuaranteeContractInformation + '\'' + "," +
                "CZY='" + this.CZY + '\'' + "," +
                "ContractInformation='" + this.ContractInformation + '\'' + "," +
                "YWWD='" + this.YWWD + '\'' +

                "}";

    }
}