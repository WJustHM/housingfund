package com.handge.housingfund.common.service.bank.enums;

/**
 * 转入交易类型
 * Created by gxy on 17-12-7.
 */
public enum TransInTxFuncEnums {
    新增申请("新增申请【转入发起】", "0"),
    确认受理("转出方确认受理【转出发起】", "1");

    private String desc;
    private String code;
    TransInTxFuncEnums(String desc, String code) {
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
