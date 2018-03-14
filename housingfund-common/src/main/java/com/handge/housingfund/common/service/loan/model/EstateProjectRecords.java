package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "EstateProjectRecords")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstateProjectRecords  implements Serializable {

    private ArrayList<EstateProjectRecordsRes> Res;  //

    public ArrayList<EstateProjectRecordsRes> getRes() {

        return this.Res;

    }


    public void setRes(ArrayList<EstateProjectRecordsRes> Res) {

        this.Res = Res;

    }


    public String toString() {

        return "EstateProjectRecords{" +

                "Res='" + this.Res + '\'' +

                "}";

    }
}