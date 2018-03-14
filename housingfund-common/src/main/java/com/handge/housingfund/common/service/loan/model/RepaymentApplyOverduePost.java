package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "RepaymentApplyOverduePost")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentApplyOverduePost  implements Serializable {

    private String YQKZJ;  //逾期款总计

    private String JKRXM;  //借款人姓名

    private String DKZH;  //贷款账号

    private String HKJE;  //还款金额

    private String YDKKRQ;  //约定扣款日期

    private ArrayList<String> overdueInformation;  //逾期信息（数组

    public String getYQKZJ() {

        return this.YQKZJ;

    }


    public void setYQKZJ(String YQKZJ) {

        this.YQKZJ = YQKZJ;

    }


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public String getDKZH() {

        return this.DKZH;

    }


    public void setDKZH(String DKZH) {

        this.DKZH = DKZH;

    }


    public String getHKJE() {

        return this.HKJE;

    }


    public void setHKJE(String HKJE) {

        this.HKJE = HKJE;

    }


    public String getYDKKRQ() {

        return this.YDKKRQ;

    }


    public void setYDKKRQ(String YDKKRQ) {

        this.YDKKRQ = YDKKRQ;

    }


    public ArrayList<String> getoverdueInformation() {

        return this.overdueInformation;

    }


    public void setoverdueInformation(ArrayList<String> overdueInformation) {

        this.overdueInformation = overdueInformation;

    }


    public String toString() {

        return "RepaymentApplyOverduePost{" +

                "YQKZJ='" + this.YQKZJ + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' + "," +
                "HKJE='" + this.HKJE + '\'' + "," +
                "YDKKRQ='" + this.YDKKRQ + '\'' + "," +
                "overdueInformation='" + this.overdueInformation + '\'' +

                "}";

    }
}