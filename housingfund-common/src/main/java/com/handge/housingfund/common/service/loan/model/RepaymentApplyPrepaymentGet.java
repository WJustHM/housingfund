package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "RepaymentApplyPrepaymentGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentApplyPrepaymentGet  implements Serializable {

    private RepaymentApplyPrepaymentGetYQHKXX YQHKXX;  //

    private RepaymentApplyPrepaymentGetTQBFHKXX TQBFHKXX;  //提前部分还款信息

    private String HKLX;  //类型（0逾期还款，1提前部分还款，2提前结清还款）

    private RepaymentApplyPrepaymentGetTQJQHKXX TQJQHKXX;  //提前结清还款信息

    public RepaymentApplyPrepaymentGetYQHKXX getYQHKXX() {

        return this.YQHKXX;

    }


    public void setYQHKXX(RepaymentApplyPrepaymentGetYQHKXX YQHKXX) {

        this.YQHKXX = YQHKXX;

    }


    public RepaymentApplyPrepaymentGetTQBFHKXX getTQBFHKXX() {

        return this.TQBFHKXX;

    }


    public void setTQBFHKXX(RepaymentApplyPrepaymentGetTQBFHKXX TQBFHKXX) {

        this.TQBFHKXX = TQBFHKXX;

    }


    public String getHKLX() {

        return this.HKLX;

    }


    public void setHKLX(String HKLX) {

        this.HKLX = HKLX;

    }


    public RepaymentApplyPrepaymentGetTQJQHKXX getTQJQHKXX() {

        return this.TQJQHKXX;

    }


    public void setTQJQHKXX(RepaymentApplyPrepaymentGetTQJQHKXX TQJQHKXX) {

        this.TQJQHKXX = TQJQHKXX;

    }


    public String toString() {

        return "RepaymentApplyPrepaymentGet{" +

                "YQHKXX='" + this.YQHKXX + '\'' + "," +
                "TQBFHKXX='" + this.TQBFHKXX + '\'' + "," +
                "HKLX='" + this.HKLX + '\'' + "," +
                "TQJQHKXX='" + this.TQJQHKXX + '\'' +

                "}";

    }
}