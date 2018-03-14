package com.handge.housingfund.common.service.bank.enums;

/**
 * 结算模式
 * Created by gxy on 17-11-8.
 */
public enum SettleTypeEnums {
    同行("同行", "1"),
    跨行实时("跨行实时", "2"),
    跨行非实时("跨行非实时", "3");

    private String desc;
    private String code;
    SettleTypeEnums(String desc, String code) {
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
