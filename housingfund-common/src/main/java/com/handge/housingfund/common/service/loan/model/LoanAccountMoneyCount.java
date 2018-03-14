package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.service.account.model.PageRes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Liujuhao on 2018/3/7.
 */

@XmlRootElement(name = "LoanAccountMoneyCount")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanAccountMoneyCount<T> extends PageRes<T> {

    private String DKYEZHJ; //贷款余额总合计
    private String DKFFZHJ; //贷款发放总合计
    private String DKYEBYHJ;    //贷款余额本页合计
    private String DKFFBYHJ;    //贷款发放本页合计

    @Override
    public String toString() {
        return "LoanAccountMoneyCount{" +
                "DKYEZHJ='" + DKYEZHJ + '\'' +
                ", DKFFZHJ='" + DKFFZHJ + '\'' +
                ", DKYEBYHJ='" + DKYEBYHJ + '\'' +
                ", DKFFBYHJ='" + DKFFBYHJ + '\'' +
                '}';
    }

    public String getDKYEZHJ() {
        return DKYEZHJ;
    }

    public void setDKYEZHJ(String DKYEZHJ) {
        this.DKYEZHJ = DKYEZHJ;
    }

    public String getDKFFZHJ() {
        return DKFFZHJ;
    }

    public void setDKFFZHJ(String DKFFZHJ) {
        this.DKFFZHJ = DKFFZHJ;
    }

    public String getDKYEBYHJ() {
        return DKYEBYHJ;
    }

    public void setDKYEBYHJ(String DKYEBYHJ) {
        this.DKYEBYHJ = DKYEBYHJ;
    }

    public String getDKFFBYHJ() {
        return DKFFBYHJ;
    }

    public void setDKFFBYHJ(String DKFFBYHJ) {
        this.DKFFBYHJ = DKFFBYHJ;
    }
}
