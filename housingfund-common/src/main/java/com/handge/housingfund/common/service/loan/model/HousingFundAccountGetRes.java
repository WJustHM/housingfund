package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HousingFundAccountGetRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingFundAccountGetRes  implements Serializable {

    private String HSLXZE;  //回收利息总额

    private String ZhuangTai;  //状态

    private String FXZE;  //罚息总额

    private Integer SYQS;  //剩余期数

    private String HSBJZE;  //回收本金总额

    private String DKZH;  //贷款账号

    private String DKYE;  //贷款余额

    private String JKRXM;  //借款人姓名

    private String DKFXDJ;  //贷款风险等级

    private String SWTYH;   //受委托银行

    private String ZJHM;    //证件号码

    private String DKFFE;   //贷款发放额

    @Override
    public String toString() {
        return "HousingFundAccountGetRes{" +
                "HSLXZE='" + HSLXZE + '\'' +
                ", ZhuangTai='" + ZhuangTai + '\'' +
                ", FXZE='" + FXZE + '\'' +
                ", SYQS=" + SYQS +
                ", HSBJZE='" + HSBJZE + '\'' +
                ", DKZH='" + DKZH + '\'' +
                ", DKYE='" + DKYE + '\'' +
                ", JKRXM='" + JKRXM + '\'' +
                ", DKFXDJ='" + DKFXDJ + '\'' +
                ", SWTYH='" + SWTYH + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                ", DKFFE='" + DKFFE + '\'' +
                '}';
    }

    public String getDKFFE() {
        return DKFFE;
    }

    public void setDKFFE(String DKFFE) {
        this.DKFFE = DKFFE;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getSWTYH() {
        return SWTYH;
    }

    public void setSWTYH(String SWTYH) {
        this.SWTYH = SWTYH;
    }

    public String getHSLXZE() {

        return this.HSLXZE;

    }


    public void setHSLXZE(String HSLXZE) {

        this.HSLXZE = HSLXZE;

    }


    public String getZhuangTai() {

        return this.ZhuangTai;

    }


    public void setZhuangTai(String ZhuangTai) {

        this.ZhuangTai = ZhuangTai;

    }


    public String getFXZE() {

        return this.FXZE;

    }


    public void setFXZE(String FXZE) {

        this.FXZE = FXZE;

    }


    public Integer getSYQS() {

        return this.SYQS;

    }


    public void setSYQS(Integer SYQS) {

        this.SYQS = SYQS;

    }


    public String getHSBJZE() {

        return this.HSBJZE;

    }


    public void setHSBJZE(String HSBJZE) {

        this.HSBJZE = HSBJZE;

    }


    public String getDKZH() {

        return this.DKZH;

    }


    public void setDKZH(String DKZH) {

        this.DKZH = DKZH;

    }


    public String getDKYE() {

        return this.DKYE;

    }


    public void setDKYE(String DKYE) {

        this.DKYE = DKYE;

    }


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }

    public String getDKFXDJ() {
        return DKFXDJ;
    }

    public void setDKFXDJ(String DKFXDJ) {
        this.DKFXDJ = DKFXDJ;
    }

}