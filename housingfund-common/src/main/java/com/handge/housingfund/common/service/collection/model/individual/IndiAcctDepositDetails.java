package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Created by 凡 on 2017/12/6.
 */
@XmlRootElement(name = "IndiAcctDepositDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class IndiAcctDepositDetails implements java.io.Serializable{

    private GetIndiAcctDepositDetailsPerson getIndiAcctDepositDetailsPerson; //基本信息

    private ArrayList<GetIndiAcctDepositDetailsDep> list; //缴存明细

    public GetIndiAcctDepositDetailsPerson getGetIndiAcctDepositDetailsPerson() {
        return getIndiAcctDepositDetailsPerson;
    }

    public void setGetIndiAcctDepositDetailsPerson(GetIndiAcctDepositDetailsPerson getIndiAcctDepositDetailsPerson) {
        this.getIndiAcctDepositDetailsPerson = getIndiAcctDepositDetailsPerson;
    }

    public ArrayList<GetIndiAcctDepositDetailsDep> getList() {
        return list;
    }

    public void setList(ArrayList<GetIndiAcctDepositDetailsDep> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "IndiAcctDepositDetails{" +
                "getIndiAcctDepositDetailsPerson=" + getIndiAcctDepositDetailsPerson +
                ", list=" + list +
                '}';
    }
}
