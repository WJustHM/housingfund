package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/10/12.
 */
@XmlRootElement(name = "LoanContractChangeBefore")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanContractChangeBefore implements Serializable {
    private static final long serialVersionUID = -1339606424616322025L;
    private BorrowerInfomation borrowerInfomation;
    private CommonBorrowerInfomation commonBorrowerInfomation;
    private LoanContractChangeQTXX loanContractChangeQTXX;

    public BorrowerInfomation getBorrowerInfomation() {
        return borrowerInfomation;
    }

    public void setBorrowerInfomation(BorrowerInfomation borrowerInfomation) {
        this.borrowerInfomation = borrowerInfomation;
    }

    public CommonBorrowerInfomation getCommonBorrowerInfomation() {
        return commonBorrowerInfomation;
    }

    public void setCommonBorrowerInfomation(CommonBorrowerInfomation commonBorrowerInfomation) {
        this.commonBorrowerInfomation = commonBorrowerInfomation;
    }

    public LoanContractChangeQTXX getLoanContractChangeQTXX() {
        return loanContractChangeQTXX;
    }

    public void setLoanContractChangeQTXX(LoanContractChangeQTXX loanContractChangeQTXX) {
        this.loanContractChangeQTXX = loanContractChangeQTXX;
    }
}
