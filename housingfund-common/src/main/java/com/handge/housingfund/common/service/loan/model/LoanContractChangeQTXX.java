package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Liujuhao on 2018/3/2.
 */
@XmlRootElement(name = "LoanContractChangeQTXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanContractChangeQTXX implements Serializable{

    private static final long serialVersionUID = -6566069632687703430L;
    String HKZH_1;
    String KHYH_1;
    String ZHHM_1;
    String YJCE_1;

    @Override
    public String toString() {
        return "LoanContractChangeQTXX{" +
                "HKZH_1='" + HKZH_1 + '\'' +
                ", KHYH_1='" + KHYH_1 + '\'' +
                ", ZHHM_1='" + ZHHM_1 + '\'' +
                ", YJCE_1='" + YJCE_1 + '\'' +
                '}';
    }

    public String getHKZH_1() {
        return HKZH_1;
    }

    public void setHKZH_1(String HKZH_1) {
        this.HKZH_1 = HKZH_1;
    }

    public String getKHYH_1() {
        return KHYH_1;
    }

    public void setKHYH_1(String KHYH_1) {
        this.KHYH_1 = KHYH_1;
    }

    public String getZHHM_1() {
        return ZHHM_1;
    }

    public void setZHHM_1(String ZHHM_1) {
        this.ZHHM_1 = ZHHM_1;
    }

    public String getYJCE_1() {
        return YJCE_1;
    }

    public void setYJCE_1(String YJCE_1) {
        this.YJCE_1 = YJCE_1;
    }

}
