package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HousingfundDepositWithdrawlClassifyReport")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingfundDepositWithdrawlClassifyReport extends TableBase implements Serializable {
    private ArrayList<HousingfundDepositWithdrawlClassifyReportDetail> list;//报表
    private String JLDW;//计量单位

    public HousingfundDepositWithdrawlClassifyReport(){

     }

    public String getJLDW() {
        return JLDW;
    }

    public void setJLDW(String JLDW) {
        this.JLDW = JLDW;
    }

    public ArrayList<HousingfundDepositWithdrawlClassifyReportDetail> getList() {
        return list;
    }

    public void setList(ArrayList<HousingfundDepositWithdrawlClassifyReportDetail> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "HousingfundDepositWithdrawlClassifyReport{" +
                "list=" + list +
                ", JLDW='" + JLDW + '\'' +
                '}';
    }
}
