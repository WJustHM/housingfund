package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "GuaranteeCompanyReviewResponses")
@XmlAccessorType(XmlAccessType.FIELD)
public class GuaranteeCompanyReviewResponses  implements Serializable {

    private ArrayList<GuaranteeCompanyReviewResponsesRes> Res;  //

    public ArrayList<GuaranteeCompanyReviewResponsesRes> getRes() {

        return this.Res;

    }


    public void setRes(ArrayList<GuaranteeCompanyReviewResponsesRes> Res) {

        this.Res = Res;

    }


    public String toString() {

        return "GuaranteeCompanyReviewResponses{" +

                "Res='" + this.Res + '\'' +

                "}";

    }
}