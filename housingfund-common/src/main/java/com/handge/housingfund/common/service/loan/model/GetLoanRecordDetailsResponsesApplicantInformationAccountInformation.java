package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetLoanRecordDetailsResponsesApplicantInformationAccountInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoanRecordDetailsResponsesApplicantInformationAccountInformation implements Serializable {

    private String GRZHZT;  //个人账户状态

    private String JZNY;  //缴至年月

    private String YJCE;  //月缴存额

    private String GRZHYE;  //个人账户余额

    private String GRJCJS;  //个人缴存基数

    private String LXZCJCYS;    //连续正常缴存月数

    public String getLXZCJCYS() {
        return LXZCJCYS;
    }

    public void setLXZCJCYS(String LXZCJCYS) {
        this.LXZCJCYS = LXZCJCYS;
    }

    public String getGRZHZT() {

        return this.GRZHZT;

    }


    public void setGRZHZT(String GRZHZT) {

        this.GRZHZT = GRZHZT;

    }


    public String getJZNY() {

        return this.JZNY;

    }


    public void setJZNY(String JZNY) {

        this.JZNY = JZNY;

    }


    public String getYJCE() {

        return this.YJCE;

    }


    public void setYJCE(String YJCE) {

        this.YJCE = YJCE;

    }


    public String getGRZHYE() {

        return this.GRZHYE;

    }


    public void setGRZHYE(String GRZHYE) {

        this.GRZHYE = GRZHYE;

    }


    public String getGRJCJS() {

        return this.GRJCJS;

    }


    public void setGRJCJS(String GRJCJS) {

        this.GRJCJS = GRJCJS;

    }


    @Override
    public String toString() {
        return "GetLoanRecordDetailsResponsesApplicantInformationAccountInformation{" +
                "GRZHZT='" + GRZHZT + '\'' +
                ", JZNY='" + JZNY + '\'' +
                ", YJCE='" + YJCE + '\'' +
                ", GRZHYE='" + GRZHYE + '\'' +
                ", GRJCJS='" + GRJCJS + '\'' +
                ", LXZCJCYS='" + LXZCJCYS + '\'' +
                '}';
    }
}