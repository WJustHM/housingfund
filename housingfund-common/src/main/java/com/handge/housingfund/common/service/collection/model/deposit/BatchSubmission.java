package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Funnyboy on 2017/8/28.
 */
@XmlRootElement(name = "BatchSubmission")
@XmlAccessorType(XmlAccessType.FIELD)
public class BatchSubmission implements Serializable {
    private static final long serialVersionUID = -2109143055861979437L;
    ArrayList<String> YWLSHJH;

    public ArrayList<String> getYWLSHJH() {
        return YWLSHJH;
    }

    public void setYWLSHJH(ArrayList<String> YWLSHJH) {
        this.YWLSHJH = YWLSHJH;
    }

    @Override
    public String toString() {
        return "BatchSubmission{" +
                "YWLSHJH=" + YWLSHJH +
                '}';
    }
}
