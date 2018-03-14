package com.handge.housingfund.common.service.account.enums;

/**
 * Created by 向超 on 2017/9/20.
 */
public enum  PolicyEnum {

    缴存比例设置(1),
    缴存额度设置(2),
    贷款年限设置(3),
    贷款比例设置(4),
    贷款额度设置(5),
    利率设置(6);


    private int code;
    PolicyEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
