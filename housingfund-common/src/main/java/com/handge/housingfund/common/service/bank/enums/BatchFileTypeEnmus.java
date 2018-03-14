package com.handge.housingfund.common.service.bank.enums;

/**
 * 批量交易文件类型
 * Created by gxy on 17-11-8.
 */
public enum BatchFileTypeEnmus {
    同行("同行","1"),
    跨行("跨行","2"),
    混合("混合","3");

    private String desc;
    private String code;

    BatchFileTypeEnmus(String desc, String code) {
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
