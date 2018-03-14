package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HousingCompanyReviewResHousingCompanyReview")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingCompanyReviewResHousingCompanyReview  implements Serializable {

    private static final long serialVersionUID = -320847316696587263L;

    private String YWLSH;  //业务流水号

    private String YWLX;    //业务类型

    private String Zhuangtai;  //状态

    private String FKGS;  //房开公司

    private String DDSJ;  //到达时间

    private String SJFLB;  //售建房类别

    private String YWWD;  //业务网点

    private String CZY;  //操作员

    private String BLSJ;  //办理时间

    private String SLSJ;    //审核受理时间

    private String SCSHY;   //上次审核员

    private String SFTS;

    private String DQSHY;

    private String DQXM;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "HousingCompanyReviewResHousingCompanyReview{" +
                "YWLSH='" + YWLSH + '\'' +
                ", YWLX='" + YWLX + '\'' +
                ", Zhuangtai='" + Zhuangtai + '\'' +
                ", FKGS='" + FKGS + '\'' +
                ", DDSJ='" + DDSJ + '\'' +
                ", SJFLB='" + SJFLB + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", CZY='" + CZY + '\'' +
                ", BLSJ='" + BLSJ + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                ", SCSHY='" + SCSHY + '\'' +
                ", SFTS='" + SFTS + '\'' +
                ", DQSHY='" + DQSHY + '\'' +
                ", DQXM='" + DQXM + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getSFTS() {
        return SFTS;
    }

    public void setSFTS(String SFTS) {
        this.SFTS = SFTS;
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

    public String getYWLX() {
        return YWLX;
    }

    public void setYWLX(String YWLX) {
        this.YWLX = YWLX;
    }

    public String getSLSJ() {
        return SLSJ;
    }

    public void setSLSJ(String SLSJ) {
        this.SLSJ = SLSJ;
    }

    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getZhuangtai() {

        return this.Zhuangtai;

    }


    public void setZhuangtai(String Zhuangtai) {

        this.Zhuangtai = Zhuangtai;

    }


    public String getFKGS() {

        return this.FKGS;

    }


    public void setFKGS(String FKGS) {

        this.FKGS = FKGS;

    }


    public String getDDSJ() {

        return this.DDSJ;

    }


    public void setDDSJ(String DDSJ) {

        this.DDSJ = DDSJ;

    }


    public String getSJFLB() {

        return this.SJFLB;

    }


    public void setSJFLB(String SJFLB) {

        this.SJFLB = SJFLB;

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


    public String getBLSJ() {

        return this.BLSJ;

    }


    public void setBLSJ(String BLSJ) {

        this.BLSJ = BLSJ;

    }

}