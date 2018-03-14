package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HousingCompanyHistory")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingCompanyHistory  implements Serializable {

    private ArrayList<HousingCompanyHistoryRes> Res;  //

    public ArrayList<HousingCompanyHistoryRes> getRes() {

        return this.Res;

    }


    public void setRes(ArrayList<HousingCompanyHistoryRes> Res) {

        this.Res = Res;

    }


    public String toString() {

        return "HousingCompanyHistory{" +

                "Res='" + this.Res + '\'' +

                "}";

    }
}