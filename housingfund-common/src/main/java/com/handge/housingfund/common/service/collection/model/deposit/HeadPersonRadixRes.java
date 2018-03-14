package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HeadPersonRadixRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadPersonRadixRes implements Serializable {

    private static final long serialVersionUID = -2660675170871642479L;
    private ArrayList<HeadPersonRadixResJCJSTZXX> JCJSTZXX;  //缴存基数调整信息

    private HeadPersonRadixResDWGJXX DWGJXX;  //单位关键信息

    public String getSHR() {
        return SHR;
    }

    public void setSHR(String SHR) {
        this.SHR = SHR;
    }

    private String SHR;//审核人

    public ArrayList<HeadPersonRadixResJCJSTZXX> getJCJSTZXX() {

        return this.JCJSTZXX;

    }


    public void setJCJSTZXX(ArrayList<HeadPersonRadixResJCJSTZXX> JCJSTZXX) {

        this.JCJSTZXX = JCJSTZXX;

    }


    public HeadPersonRadixResDWGJXX getDWGJXX() {

        return this.DWGJXX;

    }


    public void setDWGJXX(HeadPersonRadixResDWGJXX DWGJXX) {

        this.DWGJXX = DWGJXX;

    }


    public String toString() {

        return "HeadPersonRadixRes{" +

            "JCJSTZXX='" + this.JCJSTZXX + '\'' + "," +
            "DWGJXX='" + this.DWGJXX + '\'' +
            "SHR='" + this.SHR + '\'' +

            "}";

    }
}