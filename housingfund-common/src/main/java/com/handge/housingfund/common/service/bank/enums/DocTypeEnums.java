package com.handge.housingfund.common.service.bank.enums;

/**
 * 职工证件类型
 * Created by gxy on 17-12-7.
 */
public enum DocTypeEnums {
    身份证("身份证", "01"),
    军官证("军官证", "02"),
    护照("护照", "03"),
    外国人永久居留证("外国人永久居留证", "04"),
    其他("其他", "99");

    private String desc;
    private String code;
    DocTypeEnums(String desc, String code) {
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
