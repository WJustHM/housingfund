package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "EstateProjectBatchReviewInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstateProjectBatchReviewInfo  implements Serializable {

    private ArrayList<String> Ids;  //业务流水号集合

    private EstateReViewInfo ReviewInfo;  //审核信息

    public ArrayList<String> getIds() {

        return this.Ids;

    }


    public void setIds(ArrayList<String> Ids) {

        this.Ids = Ids;

    }


    public EstateReViewInfo getReviewInfo() {

        return this.ReviewInfo;

    }


    public void setReviewInfo(EstateReViewInfo ReviewInfo) {

        this.ReviewInfo = ReviewInfo;

    }


    public String toString() {

        return "EstateProjectBatchReviewInfo{" +

                "Ids='" + this.Ids + '\'' + "," +
                "ReviewInfo='" + this.ReviewInfo + '\'' +

                "}";

    }
}