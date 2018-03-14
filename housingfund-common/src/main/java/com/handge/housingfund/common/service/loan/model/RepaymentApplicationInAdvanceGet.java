package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/8/15.
 */
@XmlRootElement(name = "RepaymentApplicationInAdvanceGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentApplicationInAdvanceGet  implements Serializable {

    private static final long serialVersionUID = -8999992652686123148L;
    private String HKLX;  //类型（9逾期还款，8提前部分还款，0提前结清还款）

    private RepaymentGetInfomation repaymentGetInfomation;//逾期还款

    private EarlyRepaymentReducemonth earlyRepaymentReducemonth;//部分还款

    private EarlySettlementLoan  earlySettlementLoan;//结清还款

    public String getHKLX() {
        return HKLX;
    }

    public void setHKLX(String HKLX) {
        this.HKLX = HKLX;
    }

    public RepaymentGetInfomation getRepaymentGetInfomation() {
        return repaymentGetInfomation;
    }

    public void setRepaymentGetInfomation(RepaymentGetInfomation repaymentGetInfomation) {
        this.repaymentGetInfomation = repaymentGetInfomation;
    }

    public EarlyRepaymentReducemonth getEarlyRepaymentReducemonth() {
        return earlyRepaymentReducemonth;
    }

    public void setEarlyRepaymentReducemonth(EarlyRepaymentReducemonth earlyRepaymentReducemonth) {
        this.earlyRepaymentReducemonth = earlyRepaymentReducemonth;
    }

    public EarlySettlementLoan getEarlySettlementLoan() {
        return earlySettlementLoan;
    }

    public void setEarlySettlementLoan(EarlySettlementLoan earlySettlementLoan) {
        this.earlySettlementLoan = earlySettlementLoan;
    }

    @Override
    public String toString() {
        return "RepaymentApplicationInAdvanceGet{" +
                "HKLX='" + HKLX + '\'' +
                ", repaymentGetInfomation=" + repaymentGetInfomation +
                ", earlyRepaymentReducemonth=" + earlyRepaymentReducemonth +
                ", earlySettlementLoan=" + earlySettlementLoan +
                '}';
    }
}
