package com.handge.housingfund.common.service.bank.enums;

/**
 * 反馈信息代码
 * Created by gxy on 17-12-7.
 */
public enum TranRstCodeEnums {
    已完成("已完成", "0"),
    失败("失败", "1"),
    处理中("处理中", "2");

    private String desc;
    private String code;
    TranRstCodeEnums(String desc, String code) {
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
