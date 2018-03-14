package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HousingCompanyRecords")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingCompanyRecords  implements Serializable {

    private ArrayList<HousingCompanyRecordsRes> Res;  //

    public ArrayList<HousingCompanyRecordsRes> getRes() {

        return this.Res;

    }


    public void setRes(ArrayList<HousingCompanyRecordsRes> Res) {

        this.Res = Res;

    }


    public String toString() {

        return "HousingCompanyRecords{" +

                "Res='" + this.Res + '\'' +

                "}";

    }
}