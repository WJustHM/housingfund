package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "RepaymentApplyPrepaymentPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentApplyPrepaymentPost  implements Serializable {
    private String YWLSH;//业务流水号


    private String JKRZJHM;

    private RepaymentApplyPrepaymentPostYQHKXX YQHKXX;  //逾期还款信息

    private RepaymentApplyPrepaymentPostTQBFHKXX TQBFHKXX;  //提前部分还款信息

    private String HKLX;  //类型（9逾期还款，8提前部分还款，0提前结清还款）

    private RepaymentApplyPrepaymentPostTQJQHKXX TQJQHKXX;  //提前结清还款信息

    private String CZY;//操作员

    private String YWWD;//业务网点


    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public RepaymentApplyPrepaymentPostYQHKXX getYQHKXX() {

        return this.YQHKXX;

    }

    public String getJKRZJHM() {
        return JKRZJHM;
    }

    public void setJKRZJHM(String JKRZJHM) {
        this.JKRZJHM = JKRZJHM;
    }

    public void setYQHKXX(RepaymentApplyPrepaymentPostYQHKXX YQHKXX) {

        this.YQHKXX = YQHKXX;

    }


    public RepaymentApplyPrepaymentPostTQBFHKXX getTQBFHKXX() {

        return this.TQBFHKXX;

    }


    public void setTQBFHKXX(RepaymentApplyPrepaymentPostTQBFHKXX TQBFHKXX) {

        this.TQBFHKXX = TQBFHKXX;

    }


    public String getHKLX() {

        return this.HKLX;

    }


    public void setHKLX(String HKLX) {

        this.HKLX = HKLX;

    }


    public RepaymentApplyPrepaymentPostTQJQHKXX getTQJQHKXX() {

        return this.TQJQHKXX;

    }


    public void setTQJQHKXX(RepaymentApplyPrepaymentPostTQJQHKXX TQJQHKXX) {

        this.TQJQHKXX = TQJQHKXX;

    }


    @Override
    public String toString() {
        return "RepaymentApplyPrepaymentPost{" +
                "YWLSH='" + YWLSH + '\'' +
                ", YQHKXX=" + YQHKXX +
                ", TQBFHKXX=" + TQBFHKXX +
                ", HKLX='" + HKLX + '\'' +
                ", TQJQHKXX=" + TQJQHKXX +
                ", CZY='" + CZY + '\'' +
                ", YWWD='" + YWWD + '\'' +
                '}';
    }
}