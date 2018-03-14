package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "LoanReviewListResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanReviewListResponse  implements Serializable {

    private String JKRXM;  //借款人姓名

    private String Status;  //状态

    private String YWLSH;  //业务流水号

    private String HTDKJE;  //合同贷款金额

    private String SLSJ;  //受理时间

    private String DDSJ;  //到达时间

    private String JKRZJHM;  //借款人证件号码

    private String YWWD;  //业务网点

    private String CZY;  //操作员

    private String SFTS;   //是否可以特审(01:可以，02:不可以)

    private String SCSHY;   //上次审核员

    private String DQSHY;

    private String DQXM;

    private String SPBWJ;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LoanReviewListResponse{" +
                "JKRXM='" + JKRXM + '\'' +
                ", Status='" + Status + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", HTDKJE='" + HTDKJE + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                ", DDSJ='" + DDSJ + '\'' +
                ", JKRZJHM='" + JKRZJHM + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", CZY='" + CZY + '\'' +
                ", SFTS='" + SFTS + '\'' +
                ", SCSHY='" + SCSHY + '\'' +
                ", DQSHY='" + DQSHY + '\'' +
                ", DQXM='" + DQXM + '\'' +
                ", SPBWJ='" + SPBWJ + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getSPBWJ() {
        return SPBWJ;
    }

    public void setSPBWJ(String SPBWJ) {
        this.SPBWJ = SPBWJ;
    }

    public String getDQSHY() {
        return DQSHY;
    }

    public void setDQSHY(String DQSHY) {
        this.DQSHY = DQSHY;
    }

    public String getDQXM() {
        return DQXM;
    }

    public void setDQXM(String DQXM) {
        this.DQXM = DQXM;
    }

    public String getSCSHY() {
        return SCSHY;
    }

    public void setSCSHY(String SCSHY) {
        this.SCSHY = SCSHY;
    }

    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public String getStatus() {

        return this.Status;

    }


    public void setStatus(String Status) {

        this.Status = Status;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getHTDKJE() {

        return this.HTDKJE;

    }


    public void setHTDKJE(String HTDKJE) {

        this.HTDKJE = HTDKJE;

    }


    public String getSLSJ() {

        return this.SLSJ;

    }


    public void setSLSJ(String SLSJ) {

        this.SLSJ = SLSJ;

    }


    public String getDDSJ() {

        return this.DDSJ;

    }


    public void setDDSJ(String DDSJ) {

        this.DDSJ = DDSJ;

    }


    public String getJKRZJHM() {

        return this.JKRZJHM;

    }


    public void setJKRZJHM(String JKRZJHM) {

        this.JKRZJHM = JKRZJHM;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }

    public String getSFTS() {
        return SFTS;
    }

    public void setSFTS(String SFTS) {
        this.SFTS = SFTS;
    }

}