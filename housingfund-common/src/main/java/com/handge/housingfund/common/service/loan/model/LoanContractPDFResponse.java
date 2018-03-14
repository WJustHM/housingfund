package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "LoanContractPDFResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanContractPDFResponse implements Serializable{

    private SignContractPost orignal;

    private GetLoanRecordDetailsResponses applicationDetails;

    private String spbbh ;

    public String getGHBJJE() {
        return GHBJJE;
    }

    public void setGHBJJE(String GHBJJE) {
        this.GHBJJE = GHBJJE;
    }

    public String getYHKE() {
        return YHKE;
    }

    public void setYHKE(String YHKE) {
        this.YHKE = YHKE;
    }

    private String DKRTXDZ ; /*贷款人通讯地址*/;
    private String DKYHMC ;/*贷款银行名称?*/;

    private String DKYHQC;
    private String LXFS;/*贷款人联系方式*/

    private String GHBJJE;/*归还本金金额*/

    private String YHKE;/*月还款额*/

    public String getDKRTXDZ() {
        return DKRTXDZ;
    }

    public void setDKRTXDZ(String DKRTXDZ) {
        this.DKRTXDZ = DKRTXDZ;
    }

    public String getDKYHMC() {
        return DKYHMC;
    }

    public void setDKYHMC(String DKYHMC) {
        this.DKYHMC = DKYHMC;
    }

    public String getDKYHQC() {
        return DKYHQC;
    }

    public void setDKYHQC(String DKYHQC) {
        this.DKYHQC = DKYHQC;
    }

    public String getLXFS() {
        return LXFS;
    }

    public void setLXFS(String LXFS) {
        this.LXFS = LXFS;
    }

    public String getSpbbh() {
        return spbbh;
    }

    public void setSpbbh(String spbbh) {
        this.spbbh = spbbh;
    }

    public GetLoanRecordDetailsResponses getApplicationDetails() {
        return applicationDetails;
    }

    public void setApplicationDetails(GetLoanRecordDetailsResponses applicationDetails) {
        this.applicationDetails = applicationDetails;
    }

    @Override
    public String toString() {
        return "LoanContractPDFResponse{" +
                "orignal=" + orignal +
                '}';
    }

    public LoanContractPDFResponse() {
    }

    public SignContractPost getOrignal() { return orignal; }

    public void setOrignal(SignContractPost orignal) {
        this.orignal = orignal;
    }
}
