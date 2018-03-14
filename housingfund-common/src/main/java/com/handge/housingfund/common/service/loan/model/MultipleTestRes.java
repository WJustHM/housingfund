package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Liujuhao on 2017/9/20.
 */
@Deprecated
@XmlRootElement(name = "MultipleTestRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class MultipleTestRes implements Serializable{

    private static final long serialVersionUID = -3624225528894660705L;

    ArrayList<String> results;

    public ArrayList<String> getResults() {
        return results;
    }

    public void setResults(ArrayList<String> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "MultipleTestRes{" +
                "results=" + results +
                '}';
    }
}
