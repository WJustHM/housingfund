package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "RepaymentCalculationResponses")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentCalculationResponses  implements Serializable {

    private String DKZE;  //贷款总额

    private String HKZE;  //还款总额

    private String GJJDKLL;  //公积金贷款利率

    private String YJHK;  //月均还款

    private String FWZJ;  //房屋总价

    private String SYDKLL;  //商业贷款利率

    private String SFK;  //首付款

    private String DKYS;  //贷款月数

    public String getDKZE() {

        return this.DKZE;

    }


    public void setDKZE(String DKZE) {

        this.DKZE = DKZE;

    }


    public String getHKZE() {

        return this.HKZE;

    }


    public void setHKZE(String HKZE) {

        this.HKZE = HKZE;

    }


    public String getGJJDKLL() {

        return this.GJJDKLL;

    }


    public void setGJJDKLL(String GJJDKLL) {

        this.GJJDKLL = GJJDKLL;

    }


    public String getYJHK() {

        return this.YJHK;

    }


    public void setYJHK(String YJHK) {

        this.YJHK = YJHK;

    }


    public String getFWZJ() {

        return this.FWZJ;

    }


    public void setFWZJ(String FWZJ) {

        this.FWZJ = FWZJ;

    }


    public String getSYDKLL() {

        return this.SYDKLL;

    }


    public void setSYDKLL(String SYDKLL) {

        this.SYDKLL = SYDKLL;

    }


    public String getSFK() {

        return this.SFK;

    }


    public void setSFK(String SFK) {

        this.SFK = SFK;

    }


    public String getDKYS() {

        return this.DKYS;

    }


    public void setDKYS(String DKYS) {

        this.DKYS = DKYS;

    }


    public String toString() {

        return "RepaymentCalculationResponses{" +

                "DKZE='" + this.DKZE + '\'' + "," +
                "HKZE='" + this.HKZE + '\'' + "," +
                "GJJDKLL='" + this.GJJDKLL + '\'' + "," +
                "YJHK='" + this.YJHK + '\'' + "," +
                "FWZJ='" + this.FWZJ + '\'' + "," +
                "SYDKLL='" + this.SYDKLL + '\'' + "," +
                "SFK='" + this.SFK + '\'' + "," +
                "DKYS='" + this.DKYS + '\'' +

                "}";

    }
}