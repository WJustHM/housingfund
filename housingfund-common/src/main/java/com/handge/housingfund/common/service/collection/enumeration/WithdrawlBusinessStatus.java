package com.handge.housingfund.common.service.collection.enumeration;

/**
 * Created by Liujuhao on 2017/7/27.
 */
public enum WithdrawlBusinessStatus {

    新建("新建","01"),
    待审核("待审核", "02"),
    待入账("待入账", "03"),
    已入账("已入账", "04"),
    审核不通过("审核不通过", "05"),
    失败("失败", "06")
    ;

    private String name;

    private String code;

    WithdrawlBusinessStatus(String name, String code) {
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
