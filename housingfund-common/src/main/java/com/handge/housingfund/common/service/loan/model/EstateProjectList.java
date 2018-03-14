package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "EstateProjectList")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstateProjectList  implements Serializable {

    private ArrayList<EstateProjectListRes> Res;  //

    public ArrayList<EstateProjectListRes> getRes() {

        return this.Res;

    }


    public void setRes(ArrayList<EstateProjectListRes> Res) {

        this.Res = Res;

    }


    public String toString() {

        return "EstateProjectList{" +

                "Res='" + this.Res + '\'' +

                "}";

    }
}