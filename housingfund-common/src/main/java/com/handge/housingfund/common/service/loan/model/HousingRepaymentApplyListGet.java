package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HousingRepaymentApplyListGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingRepaymentApplyListGet  implements Serializable {

    private ArrayList<HousingRepaymentApplyListGetRes> Res;  //

    public ArrayList<HousingRepaymentApplyListGetRes> getRes() {

        return this.Res;

    }


    public void setRes(ArrayList<HousingRepaymentApplyListGetRes> Res) {

        this.Res = Res;

    }


    public String toString() {

        return "HousingRepaymentApplyListGet{" +

                "Res='" + this.Res + '\'' +

                "}";

    }
}