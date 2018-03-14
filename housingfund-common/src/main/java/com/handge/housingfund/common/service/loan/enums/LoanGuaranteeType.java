package com.handge.housingfund.common.service.loan.enums;

/**
 * Created by Liujuhao on 2017/8/16.
 */

/**
 * 贷款担保类型
 */
public enum LoanGuaranteeType {

    抵押("01"),
    质押("02"),
    保证("03"),
    其他("99");

    private String code;

    LoanGuaranteeType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
