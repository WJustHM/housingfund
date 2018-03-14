package com.handge.housingfund.common.service.collection.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Liujuhao on 2017/8/16.
 */
@XmlRootElement(name = "CollectionDeleteInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class CollectionDeleteInfo  implements Serializable {

    private ArrayList<String> YWLSHs;

    public ArrayList<String> getYWLSHs() {
        return YWLSHs;
    }

    public void setYWLSHs(ArrayList<String> YWLSHs) {
        this.YWLSHs = YWLSHs;
    }

    @Override
    public String toString() {
        return "CollectionDeleteInfo{" +
                "YWLSHs=" + YWLSHs +
                '}';
    }
}
