package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "RepaymentApplyOverdueIdGetOverdueInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentApplyOverdueIdGetOverdueInformation  implements Serializable {

    private String HKJE;  //还款金额

    private String YDKKRQ;  //约定扣款日期

    private String YQKZJ;  //逾期款总计

    private ArrayList<String> Information;  //逾期信息（数组）

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


    public String getYQKZJ() {

        return this.YQKZJ;

    }


    public void setYQKZJ(String YQKZJ) {

        this.YQKZJ = YQKZJ;

    }


    public ArrayList<String> getInformation() {

        return this.Information;

    }


    public void setInformation(ArrayList<String> Information) {

        this.Information = Information;

    }


    public String toString() {

        return "RepaymentApplyOverdueIdGetOverdueInformation{" +

                "HKJE='" + this.HKJE + '\'' + "," +
                "YDKKRQ='" + this.YDKKRQ + '\'' + "," +
                "YQKZJ='" + this.YQKZJ + '\'' + "," +
                "Information='" + this.Information + '\'' +

                "}";

    }
}