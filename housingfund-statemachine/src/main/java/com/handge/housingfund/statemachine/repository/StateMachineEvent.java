package com.handge.housingfund.statemachine.repository;

/**
 * Created by xuefei_wang on 17-9-15.
 */
public final class StateMachineEvent {
    public static final  String SUCCESS = "通过";
    public static final String FAILED = "不通过";
    public static final String REVOKE = "撤回";
    public static final String SUBMIT = "提交";
    public static final String SPECIAL = "特审";
    public static final String SAVE = "保存";
    public static final String CANCEL = "作废";
}
