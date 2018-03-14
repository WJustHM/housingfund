package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by gxy on 17-10-25.
 */
@XmlRootElement(name = "余额调节期初余额")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReconciliationInitBalance implements Serializable {
    private static final long serialVersionUID = -5040736391353693759L;

    //中心银行存款日记账余额
    private String centerAcctBal;
    //银行对账单余额
    private String bankAcctBal;

    public ReconciliationInitBalance() {
    }

    public ReconciliationInitBalance(String centerAcctBal, String bankAcctBal) {
        this.centerAcctBal = centerAcctBal;
        this.bankAcctBal = bankAcctBal;
    }

    public String getCenterAcctBal() {
        return centerAcctBal;
    }

    public void setCenterAcctBal(String centerAcctBal) {
        this.centerAcctBal = centerAcctBal;
    }

    public String getBankAcctBal() {
        return bankAcctBal;
    }

    public void setBankAcctBal(String bankAcctBal) {
        this.bankAcctBal = bankAcctBal;
    }

    @Override
    public String toString() {
        return "ReconciliationInitBalance{" +
                "centerAcctBal='" + centerAcctBal + '\'' +
                ", bankAcctBal='" + bankAcctBal + '\'' +
                '}';
    }
}
