package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by tanyi on 2017/12/19.
 */
@XmlRootElement(name = "单位未分摊余额记录")
@XmlAccessorType(XmlAccessType.FIELD)
public class FinanceRecordUnitModel implements Serializable {

    private static final long serialVersionUID = 7264580112113846762L;

    //单位账号
    private String dwzh;

    //发生额
    private BigDecimal fse = BigDecimal.ZERO;

    //未分摊月
    private BigDecimal wftye = BigDecimal.ZERO;

    //备注
    private String remark;

    //摘要
    private String summary;

    //记账凭证号
    private String jzpzh;

    //资金来源（汇补缴，暂收分摊，未分摊转移，错缴）
    private String zjly;

    private String FSSJ;//发生时间

    public FinanceRecordUnitModel() {
    }

    public FinanceRecordUnitModel(String dwzh, BigDecimal fse, BigDecimal wftye, String remark, String summary, String jzpzh, String zjly, String FSSJ) {
        this.dwzh = dwzh;
        this.fse = fse;
        this.wftye = wftye;
        this.remark = remark;
        this.summary = summary;
        this.jzpzh = jzpzh;
        this.zjly = zjly;
        this.FSSJ = FSSJ;
    }

    public String getDwzh() {
        return dwzh;
    }

    public void setDwzh(String dwzh) {
        this.dwzh = dwzh;
    }

    public BigDecimal getFse() {
        return fse;
    }

    public void setFse(BigDecimal fse) {
        this.fse = fse;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getJzpzh() {
        return jzpzh;
    }

    public void setJzpzh(String jzpzh) {
        this.jzpzh = jzpzh;
    }

    public String getZjly() {
        return zjly;
    }

    public void setZjly(String zjly) {
        this.zjly = zjly;
    }

    public String getFSSJ() {
        return FSSJ;
    }

    public void setFSSJ(String FSSJ) {
        this.FSSJ = FSSJ;
    }

    public BigDecimal getWftye() {
        return wftye;
    }

    public void setWftye(BigDecimal wftye) {
        this.wftye = wftye;
    }

    @Override
    public String toString() {
        return "FinanceRecordUnitModel{" +
                "dwzh='" + dwzh + '\'' +
                ", fse=" + fse +
                ", wftye=" + wftye +
                ", remark='" + remark + '\'' +
                ", summary='" + summary + '\'' +
                ", jzpzh='" + jzpzh + '\'' +
                ", zjly='" + zjly + '\'' +
                ", FSSJ='" + FSSJ + '\'' +
                '}';
    }
}
