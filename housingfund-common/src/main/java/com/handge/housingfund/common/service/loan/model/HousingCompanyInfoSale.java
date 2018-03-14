package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by 向超 on 2017/8/14.
 */
@XmlRootElement(name = "HousingCompanyInfoSale")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingCompanyInfoSale  implements Serializable {

    private String SFRKHYHMC;  //售房人开户银行名称

    private String SFRKHYHKHM;  //售房人开户银行开户名
    @Annotation.BankCard(name = "保证金账户")
    private String SFRZHHM;  //售房人账户号码

    public String getSFRKHYHMC() {
        return SFRKHYHMC;
    }

    public void setSFRKHYHMC(String SFRKHYHMC) {
        this.SFRKHYHMC = SFRKHYHMC;
    }

    public String getSFRKHYHKHM() {
        return SFRKHYHKHM;
    }

    public void setSFRKHYHKHM(String SFRKHYHKHM) {
        this.SFRKHYHKHM = SFRKHYHKHM;
    }

    public String getSFRZHHM() {
        return SFRZHHM;
    }

    public void setSFRZHHM(String SFRZHHM) {
        this.SFRZHHM = SFRZHHM;
    }

    @Override
    public String toString() {
        return "HousingCompanyInfoSale{" +
                "SFRKHYHMC='" + SFRKHYHMC + '\'' +
                ", SFRKHYHKHM='" + SFRKHYHKHM + '\'' +
                ", SFRZHHM='" + SFRZHHM + '\'' +
                '}';
    }
}
