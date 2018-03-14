package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "RepaymentCalculationPostAJCSJS")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentCalculationPostAJCSJS  implements Serializable {

    private String AJCS;  //按揭成数

    private String FWZJ;  //房屋总价

    public String getAJCS() {

        return this.AJCS;

    }


    public void setAJCS(String AJCS) {

        this.AJCS = AJCS;

    }


    public String getFWZJ() {

        return this.FWZJ;

    }


    public void setFWZJ(String FWZJ) {

        this.FWZJ = FWZJ;

    }


    public String toString() {

        return "RepaymentCalculationPostAJCSJS{" +

                "AJCS='" + this.AJCS + '\'' + "," +
                "FWZJ='" + this.FWZJ + '\'' +

                "}";

    }
}