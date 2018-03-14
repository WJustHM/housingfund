package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Liujuhao on 2017/8/17.
 */

@XmlRootElement(name = "LoanAccountBorrowerInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanAccountBorrowerInfo implements Serializable{

    private static final long serialVersionUID = -2811482528200027782L;

    private String JKRXM;   //借款人姓名

    private String JKRZJLX; //借款人证件类型

    private String JKRZJHM; //借款人证件号码

    private String DKZH;    //贷款账号

    private String DKFFRQ;  //贷款发放日期

    private String DKDQRQ;  //贷款到期日期

    @Override
    public String toString() {
        return "LoanAccountBorrowerInfo{" +
                "JKRXM='" + JKRXM + '\'' +
                ", JKRZJLX='" + JKRZJLX + '\'' +
                ", JKRZJHM='" + JKRZJHM + '\'' +
                ", DKZH='" + DKZH + '\'' +
                ", DKFFRQ='" + DKFFRQ + '\'' +
                ", DKDQRQ='" + DKDQRQ + '\'' +
                '}';
    }

    public String getDKFFRQ() {
        return DKFFRQ;
    }

    public void setDKFFRQ(String DKFFRQ) {
        this.DKFFRQ = DKFFRQ;
    }

    public String getDKDQRQ() {
        return DKDQRQ;
    }

    public void setDKDQRQ(String DKDQRQ) {
        this.DKDQRQ = DKDQRQ;
    }

    public String getJKRXM() {
        return JKRXM;
    }

    public void setJKRXM(String JKRXM) {
        this.JKRXM = JKRXM;
    }

    public String getJKRZJLX() {
        return JKRZJLX;
    }

    public void setJKRZJLX(String JKRZJLX) {
        this.JKRZJLX = JKRZJLX;
    }

    public String getJKRZJHM() {
        return JKRZJHM;
    }

    public void setJKRZJHM(String JKRZJHM) {
        this.JKRZJHM = JKRZJHM;
    }

    public String getDKZH() {
        return DKZH;
    }

    public void setDKZH(String DKZH) {
        this.DKZH = DKZH;
    }
}
