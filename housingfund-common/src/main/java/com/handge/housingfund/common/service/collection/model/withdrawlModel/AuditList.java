package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 周君 on 2017/7/28.
 */
@XmlRootElement(name = "AuditList")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuditList<T> implements Serializable {

    private ArrayList<T> Res;

    public ArrayList<T> getRes() {
        return Res;
    }

    public void setRes(ArrayList<T> res) {
        Res = res;
    }

    @Override
    public String toString() {
        return "AuditList{" + "Res=" + Res + '}';
    }
}
