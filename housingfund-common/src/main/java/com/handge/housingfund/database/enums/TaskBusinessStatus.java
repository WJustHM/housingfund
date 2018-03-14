package com.handge.housingfund.database.enums;

/**
 * Created by Funnyboy on 2017/8/30.
 */
public enum TaskBusinessStatus {
    待入账("待入账","01"),
    办结("办结","02"),
    入账失败("入账失败","03"),//可以转为重划扣，重新进入还款队列，回调的函数回传扣款失败信息
    重划扣("重划扣","05"),
    已入账("已入账","06"),
    已作废("已作废", "09"),
    扣款已发送("扣款已发送","08"),
    逾期("逾期","07");
    /**
     *  除了办结，其他都可能进入逾期队列
     * */


    private String name;
    private String code;

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

    TaskBusinessStatus(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
