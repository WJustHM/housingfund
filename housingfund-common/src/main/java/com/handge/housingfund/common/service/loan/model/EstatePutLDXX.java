package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "EstatePutLDXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstatePutLDXX  implements Serializable {

    private ArrayList<EstatePutLDXXEstateDetail> estateDetail;  //

    public ArrayList<EstatePutLDXXEstateDetail> getestateDetail() {

        return this.estateDetail;

    }


    public void setestateDetail(ArrayList<EstatePutLDXXEstateDetail> estateDetail) {

        this.estateDetail = estateDetail;

    }


    public String toString() {

        return "EstatePutLDXX{" +

                "estateDetail='" + this.estateDetail + '\'' +

                "}";

    }
}