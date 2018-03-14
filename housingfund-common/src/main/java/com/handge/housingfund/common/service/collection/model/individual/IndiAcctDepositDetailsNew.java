package com.handge.housingfund.common.service.collection.model.individual;

import com.handge.housingfund.common.service.account.model.PageRes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "IndiAcctDepositDetailsNew")
@XmlAccessorType(XmlAccessType.FIELD)
public class IndiAcctDepositDetailsNew<T> implements Serializable{

   private GetIndiAcctDepositDetailsPerson getIndiAcctDepositDetailsPerson;

   private PageRes<T> pageRes;

    public GetIndiAcctDepositDetailsPerson getGetIndiAcctDepositDetailsPerson() {
        return getIndiAcctDepositDetailsPerson;
    }

    public void setGetIndiAcctDepositDetailsPerson(GetIndiAcctDepositDetailsPerson getIndiAcctDepositDetailsPerson) {
        this.getIndiAcctDepositDetailsPerson = getIndiAcctDepositDetailsPerson;
    }

    public PageRes<T> getPageRes() {
        return pageRes;
    }

    public void setPageRes(PageRes<T> pageRes) {
        this.pageRes = pageRes;
    }

    @Override
    public String toString() {
        return "IndiAcctDepositDetailsNew{" +
                "getIndiAcctDepositDetailsPerson=" + getIndiAcctDepositDetailsPerson +
                ", pageRes=" + pageRes +
                '}';
    }
}
