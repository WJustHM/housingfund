package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HousingFundAccountGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingFundAccountGet  implements Serializable {

    private ArrayList<HousingFundAccountGetRes> Res;  //

    public ArrayList<HousingFundAccountGetRes> getRes() {

        return this.Res;

    }


    public void setRes(ArrayList<HousingFundAccountGetRes> Res) {

        this.Res = Res;

    }


    public String toString() {

        return "HousingFundAccountGet{" +

                "Res='" + this.Res + '\'' +

                "}";

    }
}