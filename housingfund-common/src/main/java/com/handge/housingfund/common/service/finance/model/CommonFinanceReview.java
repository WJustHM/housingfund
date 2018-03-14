package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Liujuhao on 2017/10/9.
 */

@XmlRootElement(name = "CommonFinanceReview")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommonFinanceReview implements Serializable{

    private String id;
    private String YWLSH;
    private String YWMC;
    private String ZhuangTai;
    private String CZY;
    private String DDSJ;
    private String SHSJ;
    private String DQSHY;
    private String DQXM;
    private String SCSHY;
    private String SFTS;

    @Override
    public String toString() {
        return "CommonFinanceReview{" +
                "id='" + id + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", YWMC='" + YWMC + '\'' +
                ", ZhuangTai='" + ZhuangTai + '\'' +
                ", CZY='" + CZY + '\'' +
                ", DDSJ='" + DDSJ + '\'' +
                ", SHSJ='" + SHSJ + '\'' +
                ", DQSHY='" + DQSHY + '\'' +
                ", DQXM='" + DQXM + '\'' +
                ", SCSHY='" + SCSHY + '\'' +
                ", SFTS='" + SFTS + '\'' +
                '}';
    }

    public String getSFTS() {
        return SFTS;
    }

    public void setSFTS(String SFTS) {
        this.SFTS = SFTS;
    }

    public String getSCSHY() {
        return SCSHY;
    }

    public void setSCSHY(String SCSHY) {
        this.SCSHY = SCSHY;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getYWMC() {
        return YWMC;
    }

    public void setYWMC(String YWMC) {
        this.YWMC = YWMC;
    }

    public String getZhuangTai() {
        return ZhuangTai;
    }

    public void setZhuangTai(String zhuangTai) {
        ZhuangTai = zhuangTai;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getDDSJ() {
        return DDSJ;
    }

    public void setDDSJ(String DDSJ) {
        this.DDSJ = DDSJ;
    }

    public String getSHSJ() {
        return SHSJ;
    }

    public void setSHSJ(String SHSJ) {
        this.SHSJ = SHSJ;
    }
}
