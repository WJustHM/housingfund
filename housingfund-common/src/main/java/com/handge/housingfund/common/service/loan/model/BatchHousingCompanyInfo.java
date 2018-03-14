package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "BatchHousingCompanyInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class BatchHousingCompanyInfo  implements Serializable {

    private String Action;  //操作 0提交 1删除 2 打印回执

    private ArrayList<String> ids;  //

    public String getAction() {

        return this.Action;

    }


    public void setAction(String Action) {

        this.Action = Action;

    }


    public ArrayList<String> getids() {

        return this.ids;

    }


    public void setids(ArrayList<String> ids) {

        this.ids = ids;

    }


    public String toString() {

        return "BatchHousingCompanyInfo{" +

                "Action='" + this.Action + '\'' + "," +
                "ids='" + this.ids + '\'' +

                "}";

    }
}