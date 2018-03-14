package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/7/27.
 * 描述
 */
@XmlRootElement(name = "WithdrawlsList")
@XmlAccessorType(XmlAccessType.FIELD)
public class WithdrawlsList implements Serializable {
    private ArrayList<Withdrawl> Res;

    public ArrayList<Withdrawl> getRes() {
        return Res;
    }

    public void setRes(ArrayList<Withdrawl> res) {
        Res = res;
    }

    @Override
    public String toString() {
        return "WithdrawlsList{" +
                "Res=" + Res +
                '}';
    }
}
