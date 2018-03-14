package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "EstateProjectAddSearchGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstateProjectAddSearchGet  implements Serializable {

    private String SJFLB;  //售建房类别

    private Integer FKZH;  //房开账号

    private String DWDZ;  //单位地址

    private String FKGS;  //房开公司

    public String getSJFLB() {

        return this.SJFLB;

    }


    public void setSJFLB(String SJFLB) {

        this.SJFLB = SJFLB;

    }


    public Integer getFKZH() {

        return this.FKZH;

    }


    public void setFKZH(Integer FKZH) {

        this.FKZH = FKZH;

    }


    public String getDWDZ() {

        return this.DWDZ;

    }


    public void setDWDZ(String DWDZ) {

        this.DWDZ = DWDZ;

    }


    public String getFKGS() {

        return this.FKGS;

    }


    public void setFKGS(String FKGS) {

        this.FKGS = FKGS;

    }



    public String toString() {

        return "EstateProjectAddSearchGet{" +

                "SJFLB='" + this.SJFLB + '\'' + "," +
                "FKZH='" + this.FKZH + '\'' + "," +
                "DWDZ='" + this.DWDZ + '\'' + "," +
                "FKGS='" + this.FKGS + '\'' +

                "}";

    }
}