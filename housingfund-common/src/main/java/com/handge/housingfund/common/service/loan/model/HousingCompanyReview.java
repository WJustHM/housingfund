package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HousingCompanyReview")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingCompanyReview  implements Serializable {

    private ArrayList<HousingCompanyReviewRes> Res;  //

    public ArrayList<HousingCompanyReviewRes> getRes() {

        return this.Res;

    }


    public void setRes(ArrayList<HousingCompanyReviewRes> Res) {

        this.Res = Res;

    }


    public String toString() {

        return "HousingCompanyReview{" +

                "Res='" + this.Res + '\'' +

                "}";

    }
}