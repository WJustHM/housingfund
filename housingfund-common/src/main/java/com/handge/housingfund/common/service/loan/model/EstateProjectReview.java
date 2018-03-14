package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "EstateProjectReview")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstateProjectReview  implements Serializable {

    private ArrayList<EstateProjectReviewRes> Res;  //

    public ArrayList<EstateProjectReviewRes> getRes() {

        return this.Res;

    }


    public void setRes(ArrayList<EstateProjectReviewRes> Res) {

        this.Res = Res;

    }


    public String toString() {

        return "EstateProjectReview{" +

                "Res='" + this.Res + '\'' +

                "}";

    }
}