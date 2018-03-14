package com.handge.housingfund.common.service.collection.enumeration;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/10/10.
 * 描述
 */

public enum WithdrawlMethod {
    现金提取("现金提取","01"),
    转账提取("转账提取", "02"),
    其他("其他", "99")
    ;

    private String name;

    private String code;

    WithdrawlMethod(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
