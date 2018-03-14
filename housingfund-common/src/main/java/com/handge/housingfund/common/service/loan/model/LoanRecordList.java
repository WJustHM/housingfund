package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Liujuhao on 2017/8/10.
 */

@XmlRootElement(name = "LoanRecordList")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanRecordList  implements Serializable {

    List<LoanRecord> Res;

    public List<LoanRecord> getRes() {
        return Res;
    }

    public void setRes(List<LoanRecord> res) {
        Res = res;
    }

    @Override
    public String toString() {
        return "LoanRecordList{" +
                "Res=" + Res +
                '}';
    }
}
