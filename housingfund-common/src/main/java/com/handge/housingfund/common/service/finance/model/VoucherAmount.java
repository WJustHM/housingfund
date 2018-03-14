package com.handge.housingfund.common.service.finance.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by tanyi on 2017/9/25.
 */
public class VoucherAmount implements Serializable {

    private static final long serialVersionUID = -738999689648386831L;
    private String ZhaiYao;//摘要
    private BigDecimal JinE;//金额
    private String remark;//备注

    public VoucherAmount() {
    }

    public VoucherAmount(String zhaiYao, BigDecimal jinE, String remark) {
        ZhaiYao = zhaiYao;
        JinE = jinE;
        this.remark = remark;
    }

    public String getZhaiYao() {
        return ZhaiYao;
    }

    public void setZhaiYao(String zhaiYao) {
        ZhaiYao = zhaiYao;
    }

    public BigDecimal getJinE() {
        return JinE;
    }

    public void setJinE(BigDecimal jinE) {
        JinE = jinE;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
