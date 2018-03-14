package com.handge.housingfund.common.service.bank.enums;

/**
 * 账户类别
 * Created by gxy on 17-11-8.
 */
public enum AcctClassEnums {
    对私("对私", "1"),
    对公("对公","2");

    private String desc;
    private String code;

    AcctClassEnums(String desc, String code) {
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
