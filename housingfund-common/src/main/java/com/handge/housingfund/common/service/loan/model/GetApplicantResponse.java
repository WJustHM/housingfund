package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "GetApplicantResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetApplicantResponse implements Serializable {

    private GetApplicantResponseCommonBorrowerInformation CommonBorrowerInformation;  // 共同借款人信息

    private GetApplicantResponseHouseInformation HouseInformation;  // 房屋信息

    private String YWLSH;  //业务流水号

    private ArrayList<GetApplicantResponseReviewInformation> reviewInformation;  //审核信息

    private  String    BLZL;   //办理资料  //上传资料

    private String SQSJ;  //申请时间

    private GetApplicantResponseCollateralInformation CollateralInformation;  // 担保信息

    private GetApplicantResponseCapitalInformation CapitalInformation;  // 资金信息

    private GetApplicantResponseApplicantInformation ApplicantInformation;  //贷款账号

    private String YWWD;//业务网点

    private String CZY;//操作员

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public GetApplicantResponseCommonBorrowerInformation getCommonBorrowerInformation() {

        return this.CommonBorrowerInformation;

    }


    public void setCommonBorrowerInformation(GetApplicantResponseCommonBorrowerInformation CommonBorrowerInformation) {

        this.CommonBorrowerInformation = CommonBorrowerInformation;

    }


    public GetApplicantResponseHouseInformation getHouseInformation() {

        return this.HouseInformation;

    }


    public void setHouseInformation(GetApplicantResponseHouseInformation HouseInformation) {

        this.HouseInformation = HouseInformation;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public ArrayList<GetApplicantResponseReviewInformation> getreviewInformation() {

        return this.reviewInformation;

    }


    public void setreviewInformation(ArrayList<GetApplicantResponseReviewInformation> reviewInformation) {

        this.reviewInformation = reviewInformation;

    }


    public String getSCZL() {

        return this.BLZL;

    }


    public void setSCZL(String SCZL) {

        this.BLZL = SCZL;

    }


    public GetApplicantResponseCollateralInformation getCollateralInformation() {

        return this.CollateralInformation;

    }


    public void setCollateralInformation(GetApplicantResponseCollateralInformation CollateralInformation) {

        this.CollateralInformation = CollateralInformation;

    }


    public GetApplicantResponseCapitalInformation getCapitalInformation() {

        return this.CapitalInformation;

    }


    public void setCapitalInformation(GetApplicantResponseCapitalInformation CapitalInformation) {

        this.CapitalInformation = CapitalInformation;

    }


    public GetApplicantResponseApplicantInformation getApplicantInformation() {

        return this.ApplicantInformation;

    }


    public void setApplicantInformation(GetApplicantResponseApplicantInformation ApplicantInformation) {

        this.ApplicantInformation = ApplicantInformation;

    }

    public ArrayList<GetApplicantResponseReviewInformation> getReviewInformation() {
        return reviewInformation;
    }

    public void setReviewInformation(ArrayList<GetApplicantResponseReviewInformation> reviewInformation) {
        this.reviewInformation = reviewInformation;
    }

    public String getSQSJ() {
        return SQSJ;
    }

    public void setSQSJ(String SQSJ) {
        this.SQSJ = SQSJ;
    }


    @Override
    public String toString() {
        return "GetApplicantResponse{" +
                "CommonBorrowerInformation=" + CommonBorrowerInformation +
                ", HouseInformation=" + HouseInformation +
                ", YWLSH='" + YWLSH + '\'' +
                ", reviewInformation=" + reviewInformation +
                ", BLZL='" + BLZL + '\'' +
                ", SQSJ='" + SQSJ + '\'' +
                ", CollateralInformation=" + CollateralInformation +
                ", CapitalInformation=" + CapitalInformation +
                ", ApplicantInformation=" + ApplicantInformation +
                '}';
    }
}