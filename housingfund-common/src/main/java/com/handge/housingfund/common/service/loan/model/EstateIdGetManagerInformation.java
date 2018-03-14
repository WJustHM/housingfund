package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "EstateIdGetManagerInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstateIdGetManagerInformation  implements Serializable {

    private String CZY;  //操作员

    private String YWWD;  //业务网点

    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String toString() {

        return "EstateIdGetManagerInformation{" +

                "CZY='" + this.CZY + '\'' + "," +
                "YWWD='" + this.YWWD + '\'' +

                "}";

    }
}