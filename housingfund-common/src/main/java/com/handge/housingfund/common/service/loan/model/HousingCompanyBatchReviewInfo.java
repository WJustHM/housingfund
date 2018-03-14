package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HousingCompanyBatchReviewInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingCompanyBatchReviewInfo  implements Serializable {

    private ArrayList<String> Ids;  //

    private HousingCompanyReviewInfo ReviewInfo;  //

    public ArrayList<String> getIds() {

        return this.Ids;

    }


    public void setIds(ArrayList<String> Ids) {

        this.Ids = Ids;

    }


    public HousingCompanyReviewInfo getReviewInfo() {

        return this.ReviewInfo;

    }


    public void setReviewInfo(HousingCompanyReviewInfo ReviewInfo) {

        this.ReviewInfo = ReviewInfo;

    }


    public String toString() {

        return "HousingCompanyBatchReviewInfo{" +

                "Ids='" + this.Ids + '\'' + "," +
                "ReviewInfo='" + this.ReviewInfo + '\'' +

                "}";

    }
}