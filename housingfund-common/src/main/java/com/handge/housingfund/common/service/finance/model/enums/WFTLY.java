package com.handge.housingfund.common.service.finance.model.enums;

/**
 * Created by tanyi on 2018/2/1.
 * 未分摊余额来源
 */
public enum WFTLY {

    汇补缴("汇补缴"),
    暂收分摊("暂收分摊"),
    未分摊转移("未分摊转移"),
    错缴("错缴"),
    未分摊汇补缴("未分摊汇补缴");

    String name;

    WFTLY(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
