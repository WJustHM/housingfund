package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/9/7.
 */
@XmlRootElement(name = "住房公积金缴存使用详细情况表")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingfundDepositDetail implements Serializable {

    private String XuHao;
    private String ZBMC;//指标名称
    private String DaiMa;//代码
    private String JLDW;//计量单位
    private String ShuLiang;//数量

    public HousingfundDepositDetail() {
    }

    public HousingfundDepositDetail(String xuHao, String ZBMC, String daiMa, String JLDW, String shuLiang) {
        XuHao = xuHao;
        this.ZBMC = ZBMC;
        DaiMa = daiMa;
        this.JLDW = JLDW;
        ShuLiang = shuLiang;
    }

    public String getXuHao() {
        return XuHao;
    }

    public void setXuHao(String xuHao) {
        XuHao = xuHao;
    }

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

    public String getJLDW() {
        return JLDW;
    }

    public void setJLDW(String JLDW) {
        this.JLDW = JLDW;
    }

    public String getShuLiang() {
        return ShuLiang;
    }

    public void setShuLiang(String shuLiang) {
        ShuLiang = shuLiang;
    }
}
