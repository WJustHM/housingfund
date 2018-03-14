package com.handge.housingfund.common.service.loan.model;

import java.io.Serializable;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/8/7.
 * 描述
 */
public class CommissionedDebitChange  implements Serializable {
    private String DKZH;  //贷款账户

    private String JKRXM;  //借款人姓名

    private String JKHTBH;  //借款合同编号

    private String WTKHYJCE;  //委托扣划月缴存额

    private String XWTKHYJCE;  //新委托扣划月缴存额

    public String getDKZH() {
        return DKZH;
    }

    public void setDKZH(String DKZH) {
        this.DKZH = DKZH;
    }

    public String getJKRXM() {
        return JKRXM;
    }

    public void setJKRXM(String JKRXM) {
        this.JKRXM = JKRXM;
    }

    public String getJKHTBH() {
        return JKHTBH;
    }

    public void setJKHTBH(String JKHTBH) {
        this.JKHTBH = JKHTBH;
    }

    public String getWTKHYJCE() {
        return WTKHYJCE;
    }

    public void setWTKHYJCE(String WTKHYJCE) {
        this.WTKHYJCE = WTKHYJCE;
    }

    public String getXWTKHYJCE() {
        return XWTKHYJCE;
    }

    public void setXWTKHYJCE(String XWTKHYJCE) {
        this.XWTKHYJCE = XWTKHYJCE;
    }

    @Override
    public String toString() {
        return "CommissionedDebitChange{" +
                "DKZH='" + DKZH + '\'' +
                ", JKRXM='" + JKRXM + '\'' +
                ", JKHTBH='" + JKHTBH + '\'' +
                ", WTKHYJCE='" + WTKHYJCE + '\'' +
                ", XWTKHYJCE='" + XWTKHYJCE + '\'' +
                '}';
    }
}
