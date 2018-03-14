package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HousingCompaniesList")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingCompaniesList  implements Serializable {

    private ArrayList<HousingCompaniesListRes> Res;  //

    public ArrayList<HousingCompaniesListRes> getRes() {

        return this.Res;

    }


    public void setRes(ArrayList<HousingCompaniesListRes> Res) {

        this.Res = Res;

    }


    public String toString() {

        return "HousingCompaniesList{" +

                "Res='" + this.Res + '\'' +

                "}";

    }
}