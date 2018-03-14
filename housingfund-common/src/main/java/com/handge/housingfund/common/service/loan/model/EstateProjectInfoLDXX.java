package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "EstateProjectInfoLDXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstateProjectInfoLDXX  implements Serializable {

    private ArrayList<EstateProjectInfoLDXXEstateDetail> estateDetail;  //楼栋信息集合

    public ArrayList<EstateProjectInfoLDXXEstateDetail> getestateDetail() {

        return this.estateDetail;

    }


    public void setestateDetail(ArrayList<EstateProjectInfoLDXXEstateDetail> estateDetail) {

        this.estateDetail = estateDetail;

    }


    public String toString() {

        return "EstateProjectInfoLDXX{" +

                "estateDetail='" + this.estateDetail + '\'' +

                "}";

    }
}