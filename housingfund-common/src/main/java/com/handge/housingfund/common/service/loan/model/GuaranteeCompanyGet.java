package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "GuaranteeCompanyGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class GuaranteeCompanyGet  implements Serializable {

    private ArrayList<GuaranteeCompanyGetRes> Res;  //

    public ArrayList<GuaranteeCompanyGetRes> getRes() {

        return this.Res;

    }


    public void setRes(ArrayList<GuaranteeCompanyGetRes> Res) {

        this.Res = Res;

    }


    public String toString() {

        return "GuaranteeCompanyGet{" +

                "Res='" + this.Res + '\'' +

                "}";

    }
}