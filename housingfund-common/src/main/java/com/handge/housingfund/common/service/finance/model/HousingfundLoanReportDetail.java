package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/9/29.
 */
@XmlRootElement(name = "住房公积金银行存款情况表详情")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingfundLoanReportDetail implements Serializable {

    private String XiangMu;//项目

    private String YuE;//余额

    private String LiLv;//利率

    public HousingfundLoanReportDetail() {
    }

    public HousingfundLoanReportDetail(String xiangMu, String yuE, String liLv) {
        XiangMu = xiangMu;
        YuE = yuE;
        LiLv = liLv;
    }

    public String getXiangMu() {
        return XiangMu;
    }

    public void setXiangMu(String xiangMu) {
        XiangMu = xiangMu;
    }

    public String getYuE() {
        return YuE;
    }

    public void setYuE(String yuE) {
        YuE = yuE;
    }

    public String getLiLv() {
        return LiLv;
    }

    public void setLiLv(String liLv) {
        LiLv = liLv;
    }
}
