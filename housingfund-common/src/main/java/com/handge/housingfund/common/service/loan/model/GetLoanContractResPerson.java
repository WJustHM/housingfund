package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Liujuhao on 2017/8/10.
 */

@XmlRootElement(name = "GetLoanContractResPerson")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoanContractResPerson  implements Serializable {

    private String JKRGJJZH;  //借款人公积金账号

    private String JCD;  //缴存地

    private String BLZL;  // 办理资料

    private String HKZH;  //还款账号

    private String ZHKHYHMC; //账号开户银行名称

    private String WTKHYJCE;  //委托扣划月缴存额

    private GetApplicantResponseApplicantInformationUnitInformation UnitInformation;  //单位信息

    private GetApplicantResponseApplicantInformationBorrowerInformation BorrowerInformation;  //借款人信息

    private GetApplicantResponseApplicantInformationAccountInformation AccountInformation;  //公积金账户信息

    public String getJKRGJJZH() {
        return JKRGJJZH;
    }

    public void setJKRGJJZH(String JKRGJJZH) {
        this.JKRGJJZH = JKRGJJZH;
    }

    public String getJCD() {
        return JCD;
    }

    public void setJCD(String JCD) {
        this.JCD = JCD;
    }

    public String getBLZL() {
        return BLZL;
    }

    public void setBLZL(String BLZL) {
        this.BLZL = BLZL;
    }

    public String getHKZH() {
        return HKZH;
    }

    public void setHKZH(String HKZH) {
        this.HKZH = HKZH;
    }

    public String getZHKHYHMC() {
        return ZHKHYHMC;
    }

    public void setZHKHYHMC(String ZHKHYHMC) {
        this.ZHKHYHMC = ZHKHYHMC;
    }

    public String getWTKHYJCE() {
        return WTKHYJCE;
    }

    public void setWTKHYJCE(String WTKHYJCE) {
        this.WTKHYJCE = WTKHYJCE;
    }

    public GetApplicantResponseApplicantInformationUnitInformation getUnitInformation() {
        return UnitInformation;
    }

    public void setUnitInformation(GetApplicantResponseApplicantInformationUnitInformation unitInformation) {
        UnitInformation = unitInformation;
    }

    public GetApplicantResponseApplicantInformationBorrowerInformation getBorrowerInformation() {
        return BorrowerInformation;
    }

    public void setBorrowerInformation(GetApplicantResponseApplicantInformationBorrowerInformation borrowerInformation) {
        BorrowerInformation = borrowerInformation;
    }

    public GetApplicantResponseApplicantInformationAccountInformation getAccountInformation() {
        return AccountInformation;
    }

    public void setAccountInformation(GetApplicantResponseApplicantInformationAccountInformation accountInformation) {
        AccountInformation = accountInformation;
    }

    @Override
    public String toString() {
        return "GetLoanContractResPerson{" +
                "JKRGJJZH='" + JKRGJJZH + '\'' +
                ", JCD='" + JCD + '\'' +
                ", BLZL='" + BLZL + '\'' +
                ", HKZH='" + HKZH + '\'' +
                ", ZHKHYHMC='" + ZHKHYHMC + '\'' +
                ", WTKHYJCE='" + WTKHYJCE + '\'' +
                ", UnitInformation=" + UnitInformation +
                ", BorrowerInformation=" + BorrowerInformation +
                ", AccountInformation=" + AccountInformation +
                '}';
    }
}
