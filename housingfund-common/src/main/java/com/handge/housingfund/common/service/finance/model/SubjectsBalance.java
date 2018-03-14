package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/9/5.
 */
@XmlRootElement(name = "科目汇总")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubjectsBalance implements Serializable {

    private static final long serialVersionUID = 6790087803631096783L;

    private String KMBH;//科目编号

    private String KMMC;//科目名称

    private String SYYE;//上月余额

    private String BYZJ;//本月增加

    private String BYJS;//本月减少

    private String BYYE;//本月余额

    private int KMJB;//科目级别

    public SubjectsBalance() {
    }

    public SubjectsBalance(String KMBH, String KMMC, String SYYE, String BYZJ, String BYJS, String BYYE, int KMJB) {
        this.KMBH = KMBH;
        this.KMMC = KMMC;
        this.SYYE = SYYE;
        this.BYZJ = BYZJ;
        this.BYJS = BYJS;
        this.BYYE = BYYE;
        this.KMJB = KMJB;
    }

    public String getKMBH() {
        return KMBH;
    }

    public void setKMBH(String KMBH) {
        this.KMBH = KMBH;
    }

    public String getKMMC() {
        return KMMC;
    }

    public void setKMMC(String KMMC) {
        this.KMMC = KMMC;
    }

    public String getSYYE() {
        return SYYE;
    }

    public void setSYYE(String SYYE) {
        this.SYYE = SYYE;
    }

    public String getBYZJ() {
        return BYZJ;
    }

    public void setBYZJ(String BYZJ) {
        this.BYZJ = BYZJ;
    }

    public String getBYJS() {
        return BYJS;
    }

    public void setBYJS(String BYJS) {
        this.BYJS = BYJS;
    }

    public String getBYYE() {
        return BYYE;
    }

    public void setBYYE(String BYYE) {
        this.BYYE = BYYE;
    }

    public int getKMJB() {
        return KMJB;
    }

    public void setKMJB(int KMJB) {
        this.KMJB = KMJB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubjectsBalance that = (SubjectsBalance) o;

        return KMBH != null ? KMBH.equals(that.KMBH) : that.KMBH == null;
    }

}
