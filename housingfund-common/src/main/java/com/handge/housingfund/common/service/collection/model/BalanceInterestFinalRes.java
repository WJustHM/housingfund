package com.handge.housingfund.common.service.collection.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "BalanceInterestFinalRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class BalanceInterestFinalRes  implements Serializable {

    private String YWLSH;  //操作状态

    private String balance;  //id(如：若是没有流水号id,那它就是各模块的模块id)

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }


    @Override
    public String toString() {
        return "BalanceInterestFinalRes{" +
                "YWLSH='" + YWLSH + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }
}