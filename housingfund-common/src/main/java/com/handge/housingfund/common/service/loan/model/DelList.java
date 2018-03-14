package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "DelList")
@XmlAccessorType(XmlAccessType.FIELD)
public class DelList  implements Serializable {

    private ArrayList<String> dellist;  //删除列表

    public ArrayList<String> getDellist() {
        return dellist;
    }

    public void setDellist(ArrayList<String> dellist) {
        this.dellist = dellist;
    }

    @Override
    public String toString() {
        return "DelList{" +
                "dellist=" + dellist +
                '}';
    }
}