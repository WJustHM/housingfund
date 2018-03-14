package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "RepaymentApplyOverdueIdGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentApplyOverdueIdGet  implements Serializable {

    private String DownNumInformation;  //提前部分还款-缩短期数、月还款额不变

    private String JKRXM;  //借款人姓名

    private String AllPayInformation;  //提前结清还款

    private String DownMonthInformation;  //提前部分还款-减月还款额、期数不变

    private String DKZH;  //贷款账号

    private RepaymentApplyOverdueIdGetOverdueInformation OverdueInformation;  //

    public String getDownNumInformation() {

        return this.DownNumInformation;

    }


    public void setDownNumInformation(String DownNumInformation) {

        this.DownNumInformation = DownNumInformation;

    }


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public String getAllPayInformation() {

        return this.AllPayInformation;

    }


    public void setAllPayInformation(String AllPayInformation) {

        this.AllPayInformation = AllPayInformation;

    }


    public String getDownMonthInformation() {

        return this.DownMonthInformation;

    }


    public void setDownMonthInformation(String DownMonthInformation) {

        this.DownMonthInformation = DownMonthInformation;

    }


    public String getDKZH() {

        return this.DKZH;

    }


    public void setDKZH(String DKZH) {

        this.DKZH = DKZH;

    }


    public RepaymentApplyOverdueIdGetOverdueInformation getOverdueInformation() {

        return this.OverdueInformation;

    }


    public void setOverdueInformation(RepaymentApplyOverdueIdGetOverdueInformation OverdueInformation) {

        this.OverdueInformation = OverdueInformation;

    }


    public String toString() {

        return "RepaymentApplyOverdueIdGet{" +

                "DownNumInformation='" + this.DownNumInformation + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' + "," +
                "AllPayInformation='" + this.AllPayInformation + '\'' + "," +
                "DownMonthInformation='" + this.DownMonthInformation + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' + "," +
                "OverdueInformation='" + this.OverdueInformation + '\'' +

                "}";

    }
}