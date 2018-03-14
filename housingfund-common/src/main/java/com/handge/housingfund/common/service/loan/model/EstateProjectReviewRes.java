package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "EstateProjectReviewRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstateProjectReviewRes  implements Serializable {

    private String YWLSH;  //业务流水号

    private String YWLX;    //业务类型

    private String LPMC;  //楼盘名称

    private String ZhuangTai;  //状态

    private String DDSJ;  //到达时间

    private String YWWD;  //业务网点

    private String LPDZ;  //楼盘地址

    private String CZY;  //操作员

    private String SLSJ;   //（审核）受理时间

    private String SCSHY;   //上次审核员

    private String DQSHY;

    private String DQXM;

    private String SFTS;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EstateProjectReviewRes{" +
                "YWLSH='" + YWLSH + '\'' +
                ", YWLX='" + YWLX + '\'' +
                ", LPMC='" + LPMC + '\'' +
                ", ZhuangTai='" + ZhuangTai + '\'' +
                ", DDSJ='" + DDSJ + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", LPDZ='" + LPDZ + '\'' +
                ", CZY='" + CZY + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                ", SCSHY='" + SCSHY + '\'' +
                ", DQSHY='" + DQSHY + '\'' +
                ", DQXM='" + DQXM + '\'' +
                ", SFTS='" + SFTS + '\'' +
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


    public String getLPMC() {

        return this.LPMC;

    }


    public void setLPMC(String LPMC) {

        this.LPMC = LPMC;

    }


    public String getZhuangTai() {

        return this.ZhuangTai;

    }


    public void setZhuangTai(String ZhuangTai) {

        this.ZhuangTai = ZhuangTai;

    }


    public String getDDSJ() {

        return this.DDSJ;

    }


    public void setDDSJ(String DDSJ) {

        this.DDSJ = DDSJ;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getLPDZ() {

        return this.LPDZ;

    }


    public void setLPDZ(String LPDZ) {

        this.LPDZ = LPDZ;

    }




    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }

}