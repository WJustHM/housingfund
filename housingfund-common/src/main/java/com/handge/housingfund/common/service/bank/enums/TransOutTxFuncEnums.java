package com.handge.housingfund.common.service.bank.enums;

/**
 * 转出交易类型
 * Created by gxy on 17-12-7.
 */
public enum TransOutTxFuncEnums {
    新增信息转出("新增是用于新增的转移接续【转出发起】", "0"),
    修改转出信息("修改是用于转入中心收到转出中心接续信息后，需与转出中心协商的情况【转入发起】", "1"),
    转入确认办结("转入确认办结是用于转入中心将转移接续业务办结信息反馈给转出地中心【转入发起】", "2");

    private String desc;
    private String code;
    TransOutTxFuncEnums(String desc, String code) {
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
