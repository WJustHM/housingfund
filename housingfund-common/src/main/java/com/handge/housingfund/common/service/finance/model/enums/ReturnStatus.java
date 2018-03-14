package com.handge.housingfund.common.service.finance.model.enums;

/**
 * Created by xuefei_wang on 17-8-24.
 */
public enum ReturnStatus {
    CREATE_OK("创建成功"),
    CREATE_FAIL("创建失败"),
    ALREADY_EXISTS("已经存在"),
    DELETE_OK("删除成功"),
    UNKNOW_ERRO("未知错误"),
    SUBMIT_EXISTS("重复提交"),
    SUBMIT_OK("提交成功"),
    SUBMIT_FAIL("提交失败"),
    ERRO("数据库存在多条共同属性的数据"),
    UPDATE_OK("更新成功"),
    UPDATE_FAIL("更新失败"),
    AUDIT_ERRO("可能由于事件不足以触发状态,或者权限导致"),
    AUDIT_FINISHED("处理完成"),
    AUDIT_BUSINESSPROCESS("数据库存在多条共同属性的数据"),
    FORBID("当前审核状态下，禁止操作"),
    ERRO_NOTFOUND("不存在该业务");
    private String msg;

    ReturnStatus( String msg) {

        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
