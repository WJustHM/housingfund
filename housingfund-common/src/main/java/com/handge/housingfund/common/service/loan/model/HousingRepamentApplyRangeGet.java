package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HousingRepamentApplyRangeGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingRepamentApplyRangeGet  implements Serializable {

    private ArrayList<HousingRepamentApplyRangeGetRes> Res;  //

    public ArrayList<HousingRepamentApplyRangeGetRes> getRes() {

        return this.Res;

    }


    public void setRes(ArrayList<HousingRepamentApplyRangeGetRes> Res) {

        this.Res = Res;

    }


    public String toString() {

        return "HousingRepamentApplyRangeGet{" +

                "Res='" + this.Res + '\'' +

                "}";

    }
}