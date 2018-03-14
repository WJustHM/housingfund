package com.handge.housingfund.common.service.util;

/*
 * Created by lian on 2017/7/19.
 */
@SuppressWarnings({"unused", "NonAsciiCharacters"})
public enum ReturnEnumeration {

    //归集模块
    Parameter_MISS("10101", "参数缺失 "),

    Parameter_NOT_MATCH("10102", "参数异常 "),

    Business_Status_NOT_MATCH("10103", "业务状态异常 "),

    Account_NOT_MATCH("10104", "账户状态异常 "),

    Data_MISS("10105", "数据缺失 "),

    Data_NOT_MATCH("10106", "数据异常 "),

    Business_FAILED("10107", "操作失败 "),

    Business_Type_NOT_MATCH("10108", "业务类型异常 "),

    Business_In_Process("10109", "业务已在办理 "),

    Business_Or_OtherBusiness_In_Process("10111", "业务已在办理或者该单位有其他业务正在办理"),

    Data_Already_Eeist("10110", "数据已存在 "),

    //...在此自定义添加其他错误类型

    Program_UNKNOW_ERROR("10199", "程序异常 "),

    Permission_Denied("10111", "没有操作权限 "),

    ZZJGDMData_LENGTH("10112", "组织机构代码长度为6-20个字符"),

    JBRSJHM_LENGTH("10113", "经办人手机号码长度为11位"),

    DWXX_Already_NAME("10114", "系统已存在相同的单位名字"),
    User_Defined("10115",""),
    //

    //财务模块
    Report_Unknow_Error("20101", "可能存在多条不期望数据"),

    //状态机
    Authentication_Failed("30101","没有操作权限"),
    StateMachineConfig_Unknow_Error("30102","业务流程异常"),
    StateMachineConfig_Retryable_Error("30103","请稍后重试");

    ReturnEnumeration(String code, String message) {

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
