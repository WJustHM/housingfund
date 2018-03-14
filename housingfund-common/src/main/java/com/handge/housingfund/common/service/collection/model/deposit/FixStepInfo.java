package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/8/28.
 */
@XmlRootElement(name = "FixStepInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class FixStepInfo implements Serializable {

    String YWLSH;

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    @Override
    public String toString() {
        return "FixStepInfo{" +
                "YWLSH='" + YWLSH + '\'' +
                '}';
    }
}
