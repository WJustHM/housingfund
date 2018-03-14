package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "LoanContractListResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanContractListResponse  implements Serializable {

    private ArrayList<LoanContractListResponseRes> Res;  //

    public ArrayList<LoanContractListResponseRes> getRes() {

        return this.Res;

    }


    public void setRes(ArrayList<LoanContractListResponseRes> Res) {

        this.Res = Res;

    }


    public String toString() {

        return "LoanContractListResponse{" +

                "Res='" + this.Res + '\'' +

                "}";

    }
}