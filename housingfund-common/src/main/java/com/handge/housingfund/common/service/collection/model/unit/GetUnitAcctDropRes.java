package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetUnitAcctDropRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitAcctDropRes  implements Serializable{

    private GetUnitAcctDropResDWXHXX DWXHXX;  //

    public GetUnitAcctDropResDWXHXX getDWXHXX() {

        return this.DWXHXX;

    }


    public void setDWXHXX(GetUnitAcctDropResDWXHXX DWXHXX) {

        this.DWXHXX = DWXHXX;

    }


    public String toString() {

        return "GetUnitAcctDropRes{" +

                "DWXHXX='" + this.DWXHXX + '\'' +

                "}";

    }
}