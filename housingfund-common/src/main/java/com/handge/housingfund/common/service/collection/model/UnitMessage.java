package com.handge.housingfund.common.service.collection.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by 凡 on 2017/10/7.
 */
@XmlRootElement(name = "UnitMessage")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitMessage {

    private UnitBaseMessage unitBaseMessage;

    private UnitAccountMessage unitAccountMessage;

    public UnitBaseMessage getUnitBaseMessage() {
        return unitBaseMessage;
    }

    public void setUnitBaseMessage(UnitBaseMessage unitBaseMessage) {
        this.unitBaseMessage = unitBaseMessage;
    }

    public UnitAccountMessage getUnitAccountMessage() {
        return unitAccountMessage;
    }

    public void setUnitAccountMessage(UnitAccountMessage unitAccountMessage) {
        this.unitAccountMessage = unitAccountMessage;
    }

    @Override
    public String toString() {
        return "UnitMessage{" +
                "unitBaseMessage='" + unitBaseMessage + '\'' +
                ", unitAccountMessage='" + unitAccountMessage + '\'' +
                '}';
    }
}
