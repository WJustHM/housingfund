package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "EstateIdGetLDXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstateIdGetLDXX  implements Serializable {

    private ArrayList<EstateIdGetLDXXEstateDetail> estateDetail;  //

    public ArrayList<EstateIdGetLDXXEstateDetail> getestateDetail() {

        return this.estateDetail;

    }


    public void setestateDetail(ArrayList<EstateIdGetLDXXEstateDetail> estateDetail) {

        this.estateDetail = estateDetail;

    }


    public String toString() {

        return "EstateIdGetLDXX{" +

                "estateDetail='" + this.estateDetail + '\'' +

                "}";

    }
}