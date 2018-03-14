package com.handge.housingfund.common.service.enumeration;

/*
 * Created by lian on 2017/7/19.
 */
@SuppressWarnings({"unused", "NonAsciiCharacters"})
public enum ErrorEnumeration {

    //公共模块
    Parameter_MISS("10101", "参数缺失："),

    Parameter_NOT_MATCH("10102", "参数异常："),

    Business_Status_NOT_MATCH("10103", "业务状态异常"),

    Account_NOT_MATCH("10104", "账户状态异常"),

    Data_MISS("10105", "数据缺失："),

    Data_NOT_MATCH("10106", "数据异常："),

    Business_FAILED("10107", "操作失败："),

    Business_Type_NOT_MATCH("10108", "业务类型异常"),

    //...在此自定义添加其他错误类型

    Program_UNKNOW_ERROR("10199", "程序异常：");


    //


    ErrorEnumeration(String code, String message) {

        this.code = code;

        this.message = message;
    }

    private String code;

    private String message;

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

}
