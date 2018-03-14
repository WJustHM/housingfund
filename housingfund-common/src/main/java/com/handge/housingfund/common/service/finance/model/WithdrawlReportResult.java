package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "提取分类结果")
@XmlAccessorType(XmlAccessType.FIELD)
public class WithdrawlReportResult implements Serializable {
    private String ShuLiang;
    private String JinE;
    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
