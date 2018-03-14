package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by 向超 on 2017/10/8.
 */
@XmlRootElement(name = "GetIndiAcctDepositDetailsRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiAcctDepositDetailsRes implements java.io.Serializable{

    private GetIndiAcctDepositDetailsPerson getIndiAcctDepositDetailsPerson; //基本信息

    private GetIndiAcctDepositDetailsDep getIndiAcctDepositDetailsDep; //缴存明细

    public GetIndiAcctDepositDetailsPerson getGetIndiAcctDepositDetailsPerson() {
        return getIndiAcctDepositDetailsPerson;
    }

    public void setGetIndiAcctDepositDetailsPerson(GetIndiAcctDepositDetailsPerson getIndiAcctDepositDetailsPerson) {
        this.getIndiAcctDepositDetailsPerson = getIndiAcctDepositDetailsPerson;
    }

    public GetIndiAcctDepositDetailsDep getGetIndiAcctDepositDetailsDep() {
        return getIndiAcctDepositDetailsDep;
    }

    public void setGetIndiAcctDepositDetailsDep(GetIndiAcctDepositDetailsDep getIndiAcctDepositDetailsDep) {
        this.getIndiAcctDepositDetailsDep = getIndiAcctDepositDetailsDep;
    }

    @Override
    public String toString() {
        return "GetIndiAcctDepositDetailsRes{" +
                "getIndiAcctDepositDetailsPerson=" + getIndiAcctDepositDetailsPerson +
                ", getIndiAcctDepositDetailsDep=" + getIndiAcctDepositDetailsDep +
                '}';
    }
}
