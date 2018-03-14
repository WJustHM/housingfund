package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "HousingfundDepositWithdrawlClassifyReportDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingfundDepositWithdrawlClassifyReportDetail implements Serializable {
    private String ZBMC;//指标名称
    private String DaiMa;//代码
    private String ShuLiang;//数量
    private String JinE;//金额

    public String getZBMC() {
        return ZBMC;
    }

    public void setZBMC(String ZBMC) {
        this.ZBMC = ZBMC;
    }

    public String getDaiMa() {
        return DaiMa;
    }

    public void setDaiMa(String daiMa) {
        DaiMa = daiMa;
    }

    public String getShuLiang() {
        return ShuLiang;
    }

    public void setShuLiang(String shuLiang) {
        ShuLiang = shuLiang;
    }

    public String getJinE() {
        return JinE;
    }

    public void setJinE(String jinE) {
        JinE = jinE;
    }


    @Override
    public String toString() {
        return "HousingfundDepositWithdrawlClassifyReportDetail{" +
                "ZBMC='" + ZBMC + '\'' +
                ", DaiMa='" + DaiMa + '\'' +
                ", ShuLiang='" + ShuLiang + '\'' +
                ", JinE='" + JinE + '\'' +
                '}';
    }
}
