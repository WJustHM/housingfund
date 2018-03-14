package com.handge.housingfund.common.service.collection.model.deposit;

import com.handge.housingfund.common.service.account.model.PageRes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by tanyi on 2017/12/17.
 */

@XmlRootElement(name = "SumBalances")
@XmlAccessorType(XmlAccessType.FIELD)
public class SumBalances<T> extends PageRes<T> {


    private String WFTHJ;//未分摊余额合计

    private String DWZHYEHJ;//单位账户余额合计

    public SumBalances() {
    }

    public SumBalances(String WFTHJ, String DWZHYEHJ) {
        this.WFTHJ = WFTHJ;
        this.DWZHYEHJ = DWZHYEHJ;
    }

    public String getWFTHJ() {
        return WFTHJ;
    }

    public void setWFTHJ(String WFTHJ) {
        this.WFTHJ = WFTHJ;
    }

    public String getDWZHYEHJ() {
        return DWZHYEHJ;
    }

    public void setDWZHYEHJ(String DWZHYEHJ) {
        this.DWZHYEHJ = DWZHYEHJ;
    }
}
