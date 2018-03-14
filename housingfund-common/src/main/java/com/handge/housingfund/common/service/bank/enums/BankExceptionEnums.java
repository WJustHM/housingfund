package com.handge.housingfund.common.service.bank.enums;

/**
 * Created by Administrator on 2017/8/31.
 */
public enum  BankExceptionEnums {
    转换发送报文出错("转换发送报文出错"),
    发送请求出错("发送请求出错"),
    接收回复出错("接收回复出错"),
    转换回复报文出错("转换回复报文出错"),
    获取发送发流水号失败("获取发送发流水号失败"),
    未查询到原业务流水号("未查询到原业务流水号");

    private String desc;
    BankExceptionEnums(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
