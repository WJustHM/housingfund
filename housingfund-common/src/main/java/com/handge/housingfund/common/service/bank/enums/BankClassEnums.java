package com.handge.housingfund.common.service.bank.enums;

/**
 * 账户行别
 * Created by gxy on 17-11-8.
 */
public enum BankClassEnums {
    本行("本行","0"),
    跨行("跨行","1");

    private String desc;
    private String code;

    BankClassEnums(String desc, String code) {
        this.desc = desc;
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }
}
