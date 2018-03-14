package com.handge.housingfund.common.service.loan.enums;

/**
 * Created by Liujuhao on 2017/8/16.
 */

/**
 * 还款方式枚举
 */
public enum RepaymentMethod {

    等额本息("等额本息","01"),
    等额本金("等额本金","02"),
    一次还款付息("一次还款付息","03"),
    自由还款方式("自由还款方式","04"),
    其他("其他","99");

    private String code;

    private String name;

    RepaymentMethod(String name,String code) {

        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
