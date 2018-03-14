package com.handge.housingfund.common.service.collection.model.unit;

import com.handge.housingfund.common.service.collection.model.deposit.UnitPayCallPost;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Funnyboy on 2017/8/28.
 */
@XmlRootElement(name = "BatchSubmissionPayCall")
@XmlAccessorType(XmlAccessType.FIELD)
public class BatchSubmissionPayCall implements Serializable {
   private ArrayList<UnitPayCallPost> payCalls;

    public ArrayList<UnitPayCallPost> getPayCalls() {
        return payCalls;
    }

    public void setPayCalls(ArrayList<UnitPayCallPost> payCalls) {
        this.payCalls = payCalls;
    }

    @Override
    public String toString() {
        return "BatchSubmissionPayCall{" +
                "payCalls=" + payCalls +
                '}';
    }
}
