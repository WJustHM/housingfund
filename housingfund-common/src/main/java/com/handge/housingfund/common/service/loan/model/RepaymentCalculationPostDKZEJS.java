package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "RepaymentCalculationPostDKZEJS")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentCalculationPostDKZEJS  implements Serializable {

    private String DKZE;  //贷款总额

    public String getDKZE() {

        return this.DKZE;

    }


    public void setDKZE(String DKZE) {

        this.DKZE = DKZE;

    }


    public String toString() {

        return "RepaymentCalculationPostDKZEJS{" +

                "DKZE='" + this.DKZE + '\'' +

                "}";

    }
}