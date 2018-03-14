package com.handge.housingfund.common.service.collection.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by 凡 on 2017/10/9.
 */
@XmlRootElement(name = "InventoryChangeList")
@XmlAccessorType(XmlAccessType.FIELD)
public class InventoryChangeList implements Serializable {

    private ArrayList<InventoryChangeDetail> changeDetail;

    private BigDecimal FSEHJ;

    public ArrayList<InventoryChangeDetail> getChangeDetail() {
        return changeDetail;
    }

    public void setChangeDetail(ArrayList<InventoryChangeDetail> changeDetail) {
        this.changeDetail = changeDetail;
    }

    public BigDecimal getFSEHJ() {
        return FSEHJ;
    }

    public void setFSEHJ(BigDecimal FSEHJ) {
        this.FSEHJ = FSEHJ;
    }

    @Override
    public String toString() {
        return "InventoryChangeList{" +
                "changeDetail=" + changeDetail +
                '}';
    }
}
