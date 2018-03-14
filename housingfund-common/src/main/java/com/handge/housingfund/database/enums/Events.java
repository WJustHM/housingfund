package com.handge.housingfund.database.enums;

/**
 * Created by 周君 on 2017/8/18.
 */
public enum Events {
    通过("通过"),
    撤回("撤回"),
    作废("作废"),
    不通过("不通过"),
    入账成功("入账成功"),
    //仅初始状态使用
    提交("提交"),

    保存("保存"),

    特审("特审");
    private String event;

    public String getEvent() {
        return event;
    }

    Events(String event){
        this.event = event;
    }
}
