package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static org.bouncycastle.asn1.x509.X509ObjectIdentifiers.id;

/**
 * Created by 向超 on 2017/9/20.
 */
@XmlRootElement(name = "PolicyCommonRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class PolicyCommonRes implements Serializable{
    private String ID;
    private String DQZ;
    private String XGZ;
    private String SXRQ;
    private String SJXMC;

    public String getSJXMC() {
        return SJXMC;
    }

    public void setSJXMC(String SJXMC) {
        this.SJXMC = SJXMC;
    }

    public String getSjxmc() {
        return id;
    }

    public void setSjxmc(String sjxmc) {
        this.ID = sjxmc;
    }

    public String getDqz() {
        return DQZ;
    }

    public void setDqz(String dqz) {
        this.DQZ = dqz;
    }

    public String getXgz() {
        return XGZ;
    }

    public void setXgz(String xgz) {
        this.XGZ = xgz;
    }

    public String getSxrq() {
        return SXRQ;
    }

    public void setSxrq(String sxrq) {
        this.SXRQ = sxrq;
    }

    @Override
    public String toString() {
        return "PolicyCommonRes{" +
                "ID='" + ID + '\'' +
                ", DQZ='" + DQZ + '\'' +
                ", XGZ='" + XGZ + '\'' +
                ", SXRQ='" + SXRQ + '\'' +
                ", SJXMC='" + SJXMC + '\'' +
                '}';
    }
}
