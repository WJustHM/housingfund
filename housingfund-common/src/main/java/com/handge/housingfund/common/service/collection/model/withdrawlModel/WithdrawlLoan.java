package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/11/21.
 * 描述
 */
public class WithdrawlLoan implements Serializable {
    private String grzh;//个人账号
    private BigDecimal fse;
    private BigDecimal fslxe;//利息

    public String getGrzh() {
        return grzh;
    }

    public void setGrzh(String grzh) {
        this.grzh = grzh;
    }

    public BigDecimal getFse() {
        return fse;
    }

    public void setFse(BigDecimal fse) {
        this.fse = fse;
    }

    public BigDecimal getFslxe() {
        return fslxe;
    }

    public void setFslxe(BigDecimal fslxe) {
        this.fslxe = fslxe;
    }
}
