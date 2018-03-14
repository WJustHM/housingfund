package com.handge.housingfund.common.service.loan.model;

import java.math.BigDecimal;

/**
 * Created by Funnyboy on 2017/10/7.
 */
public class SettlePartialRepayments {
    private BigDecimal TQHKJE;
    private BigDecimal TQHBJE;
    private BigDecimal TQHKLX;

    public SettlePartialRepayments() {
    }

    public SettlePartialRepayments(BigDecimal TQHKJE, BigDecimal TQHBJE, BigDecimal TQHKLX) {
        this.TQHKJE = TQHKJE;
        this.TQHBJE = TQHBJE;
        this.TQHKLX = TQHKLX;
    }

    public BigDecimal getTQHKJE() {
        return TQHKJE;
    }

    public void setTQHKJE(BigDecimal TQHKJE) {
        this.TQHKJE = TQHKJE;
    }

    public BigDecimal getTQHBJE() {
        return TQHBJE;
    }

    public void setTQHBJE(BigDecimal TQHBJE) {
        this.TQHBJE = TQHBJE;
    }

    public BigDecimal getTQHKLX() {
        return TQHKLX;
    }

    public void setTQHKLX(BigDecimal TQHKLX) {
        this.TQHKLX = TQHKLX;
    }
}
