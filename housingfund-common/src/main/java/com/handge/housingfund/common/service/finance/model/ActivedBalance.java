package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by gxy on 17-9-27.
 */
@XmlRootElement(name = "活期余额")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActivedBalance implements Serializable{

    private static final long serialVersionUID = 5009442967308371407L;
    /**
     * 账号
     */
    private String AcctNo;
    /**
     * 账户户名
     */
    private String AcctName;
    /**
     * 账户余额
     */
    private String AcctBal;
    /**
     * 账户可用额度
     */
    private String AcctRestBal;
    /**
     * 账户透支额
     */
    private String AcctOverBal;
    /**
     * 账户状态
     */
    private String AcctStatus;

    public String getAcctNo() {
        return AcctNo;
    }

    public void setAcctNo(String acctNo) {
        AcctNo = acctNo;
    }

    public String getAcctName() {
        return AcctName;
    }

    public void setAcctName(String acctName) {
        AcctName = acctName;
    }

    public String getAcctBal() {
        return AcctBal;
    }

    public void setAcctBal(String acctBal) {
        AcctBal = acctBal;
    }

    public String getAcctRestBal() {
        return AcctRestBal;
    }

    public void setAcctRestBal(String acctRestBal) {
        AcctRestBal = acctRestBal;
    }

    public String getAcctOverBal() {
        return AcctOverBal;
    }

    public void setAcctOverBal(String acctOverBal) {
        AcctOverBal = acctOverBal;
    }

    public String getAcctStatus() {
        return AcctStatus;
    }

    public void setAcctStatus(String acctStatus) {
        AcctStatus = acctStatus;
    }
}
