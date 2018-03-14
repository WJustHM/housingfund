package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "EstateProjectHistory")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstateProjectHistory  implements Serializable {

    private ArrayList<EstateProjectHistoryRes> Res;  //

    public ArrayList<EstateProjectHistoryRes> getRes() {

        return this.Res;

    }


    public void setRes(ArrayList<EstateProjectHistoryRes> Res) {

        this.Res = Res;

    }


    public String toString() {

        return "EstateProjectHistory{" +

                "Res='" + this.Res + '\'' +

                "}";

    }
}