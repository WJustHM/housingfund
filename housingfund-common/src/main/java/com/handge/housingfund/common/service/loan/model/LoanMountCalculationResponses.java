package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "LoanMountCalculationResponses")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanMountCalculationResponses  implements Serializable {

    private String ZFGJJKDKED;  //住房公积金可贷款额度

    private String ZFGJJKDKNX;  //住房公积金可贷款年限

    public String getZFGJJKDKED() {

        return this.ZFGJJKDKED;

    }


    public void setZFGJJKDKED(String ZFGJJKDKED) {

        this.ZFGJJKDKED = ZFGJJKDKED;

    }


    public String getZFGJJKDKNX() {

        return this.ZFGJJKDKNX;

    }


    public void setZFGJJKDKNX(String ZFGJJKDKNX) {

        this.ZFGJJKDKNX = ZFGJJKDKNX;

    }


    public String toString() {

        return "LoanMountCalculationResponses{" +

                "ZFGJJKDKED='" + this.ZFGJJKDKED + '\'' + "," +
                "ZFGJJKDKNX='" + this.ZFGJJKDKNX + '\'' +

                "}";

    }
}