package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HousingCompanyReviewRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingCompanyReviewRes  implements Serializable {

    private HousingCompanyReviewResHousingCompanyReview HousingCompanyReview;  //

    public HousingCompanyReviewResHousingCompanyReview getHousingCompanyReview() {

        return this.HousingCompanyReview;

    }


    public void setHousingCompanyReview(HousingCompanyReviewResHousingCompanyReview HousingCompanyReview) {

        this.HousingCompanyReview = HousingCompanyReview;

    }


    public String toString() {

        return "HousingCompanyReviewRes{" +

                "HousingCompanyReview='" + this.HousingCompanyReview + '\'' +

                "}";

    }
}